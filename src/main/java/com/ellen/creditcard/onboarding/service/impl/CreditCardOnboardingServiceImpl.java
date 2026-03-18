package com.ellen.creditcard.onboarding.service.impl;

import com.ellen.creditcard.onboarding.constant.ScoringConstants;
import com.ellen.creditcard.onboarding.dto.CreditCardApplicationRequest;
import com.ellen.creditcard.onboarding.dto.CreditCardApplicationResponse;
import com.ellen.creditcard.onboarding.entity.CreditCardApplication;
import com.ellen.creditcard.onboarding.enums.ApprovalStatus;
import com.ellen.creditcard.onboarding.enums.CheckStatus;
import com.ellen.creditcard.onboarding.integration.*;
import com.ellen.creditcard.onboarding.repository.CreditCardApplicationRepository;
import com.ellen.creditcard.onboarding.service.CreditCardOnboardingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card onboarding service implementation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCardOnboardingServiceImpl implements CreditCardOnboardingService {

    private final CreditCardApplicationRepository applicationRepository;

    private final BehaviorAnalysisServiceIntegrate  behaviorAnalysisServiceIntegrate;

    private final ComplianceInfoServiceIntegrate complianceInfoServiceIntegrate;

    private final EmploymentProfileServiceIntegrate employmentProfileServiceIntegrate;

    private final IdentityServiceIntegrate identityServiceIntegrate;

    private final RiskEvaluationServiceIntegrate riskEvaluationServiceIntegrate;

    private static final String UPLOAD_DIR = "uploads/bank_statements/";

    @Override
    @Transactional
    public CreditCardApplicationResponse submitApplication(CreditCardApplicationRequest request) {
        // 1. Initialize application entity and copy basic info
        CreditCardApplication application = new CreditCardApplication();
        BeanUtils.copyProperties(request, application);

        // 2. Save bank statement file and record storage path
        String bankStatementPath = saveBankStatement(request.getBankStatement());
        application.setBankStatementPath(bankStatementPath);

        // 3. Execute all third-party checks via mock services
        performThirdPartyChecks(application, request);

        // 4. Calculate total score and determine approval status
        calculateTotalScoreAndApproval(application);

        // 5. Save application to in-memory database
        CreditCardApplication saved = applicationRepository.save(application);
        log.info("Application submitted successfully with ID - {}", saved.getId());

        // 6. Convert entity to response DTO and return
        return convertToResponse(application);
    }

    @Override
    public CreditCardApplicationResponse getApplicationResult(Long applicationId) {
        CreditCardApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID - " + applicationId));
        return convertToResponse(application);
    }

    /**
     * Execute all third-party mock checks
     * Mandatory: Identity verification must pass to run other checks
     */
    private void performThirdPartyChecks(CreditCardApplication application, CreditCardApplicationRequest request) {
        // Mandatory check: Identity verification (ICA)
        CheckStatus idStatus = identityServiceIntegrate.verifyIdentity(request.getEmiratesId());
        application.setIdentityVerification(idStatus);

        // Run other checks ONLY if identity verification passes (per document requirements)
        if (CheckStatus.YES.equals(idStatus)) {
            application.setComplianceCheck(complianceInfoServiceIntegrate.complianceBlacklistCheck(request.getEmiratesId(), request.getName()));
            application.setEmploymentVerification(employmentProfileServiceIntegrate.verifyEmployment(request.getEmploymentDetails()));
            application.setRiskEvaluationScore(riskEvaluationServiceIntegrate.evaluateCreditRisk(request.getIncome()));
            application.setBehavioralAnalysisScore(behaviorAnalysisServiceIntegrate.analyzeBehavior(request.getBankStatement()));
        } else {
            // Set other checks to PENDING if identity verification fails
            application.setComplianceCheck(CheckStatus.PENDING);
            application.setEmploymentVerification(CheckStatus.PENDING);
            application.setRiskEvaluationScore(0.0);
            application.setBehavioralAnalysisScore(0.0);
        }
    }

    /**
     * Calculate total score and determine approval status per document rules
     * Scoring Rule: Identity(20%) + Employment(20%) + Compliance(20%) + Risk(0-20%) + Behavioral(0-20%)
     * Approval Thresholds: STP(>90) | NEAR_STP(75-90) | MANUAL_REVIEW(50-75) | REJECTED(<50/Identity Fail)
     */
    private void calculateTotalScoreAndApproval(CreditCardApplication application) {
        double totalScore = 0.0;
        CheckStatus idStatus = application.getIdentityVerification();

        // Mandatory rejection for failed identity check
        if (CheckStatus.NO.equals(idStatus)) {
            application.setTotalScore(0.0);
            application.setApprovalStatus(ApprovalStatus.REJECTED);
            application.setApprovalComments("Rejected: Identity verification failed (mandatory check)");
            return;
        }

        // Calculate scores using constants from ScoringConstants class
        totalScore += ScoringConstants.IDENTITY_SCORE_WEIGHT;

        if (CheckStatus.YES.equals(application.getEmploymentVerification())) {
            totalScore += ScoringConstants.EMPLOYMENT_SCORE_WEIGHT;
        }

        if (CheckStatus.YES.equals(application.getComplianceCheck())) {
            totalScore += ScoringConstants.COMPLIANCE_SCORE_WEIGHT;
        }

        totalScore += (application.getRiskEvaluationScore() / ScoringConstants.SCORE_NORMALIZATION_FACTOR)
                * ScoringConstants.RISK_SCORE_MAX_WEIGHT;

        totalScore += (application.getBehavioralAnalysisScore() / ScoringConstants.SCORE_NORMALIZATION_FACTOR)
                * ScoringConstants.BEHAVIORAL_SCORE_MAX_WEIGHT;

        // Round score using constants
        totalScore = Math.round(totalScore * ScoringConstants.ROUNDING_MULTIPLIER)
                / ScoringConstants.ROUNDING_MULTIPLIER;
        application.setTotalScore(totalScore);

        // Determine approval status using threshold constants
        if (totalScore > ScoringConstants.STP_THRESHOLD) {
            application.setApprovalStatus(ApprovalStatus.STP);
            application.setApprovalComments("Approved (STP): Card issued automatically");
        } else if (totalScore >= ScoringConstants.NEAR_STP_LOWER_THRESHOLD
                && totalScore <= ScoringConstants.NEAR_STP_UPPER_THRESHOLD) {
            application.setApprovalStatus(ApprovalStatus.NEAR_STP);
            application.setApprovalComments("Approved (Near-STP): Card issued, credit limit to be set manually");
        } else if (totalScore >= ScoringConstants.MANUAL_REVIEW_LOWER_THRESHOLD
                && totalScore < ScoringConstants.MANUAL_REVIEW_UPPER_THRESHOLD) {
            application.setApprovalStatus(ApprovalStatus.MANUAL_REVIEW);
            application.setApprovalComments("Pending: Manual review required");
        } else if (totalScore < ScoringConstants.REJECTION_THRESHOLD) {
            application.setApprovalStatus(ApprovalStatus.REJECTED);
            application.setApprovalComments("Rejected: Total score below 50%");
        }
    }

    /**
     * Save bank statement file to local directory
     */
    private String saveBankStatement(org.springframework.web.multipart.MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File destFile = new File(UPLOAD_DIR + System.currentTimeMillis() + "_" + file.getOriginalFilename());
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(destFile));
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            log.error("Failed to save bank statement file", e);
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    /**
     * Convert database entity to response DTO
     */
    private CreditCardApplicationResponse convertToResponse(CreditCardApplication application) {
        CreditCardApplicationResponse response = new CreditCardApplicationResponse();
        BeanUtils.copyProperties(application, response);
        response.setApplicationId(application.getId());
        return response;
    }
}