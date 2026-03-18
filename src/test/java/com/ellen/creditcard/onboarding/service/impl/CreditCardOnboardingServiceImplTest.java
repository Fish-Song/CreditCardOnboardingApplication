package com.ellen.creditcard.onboarding.service.impl;

import com.ellen.creditcard.onboarding.dto.CreditCardApplicationRequest;
import com.ellen.creditcard.onboarding.entity.CreditCardApplication;
import com.ellen.creditcard.onboarding.enums.ApprovalStatus;
import com.ellen.creditcard.onboarding.enums.CheckStatus;
import com.ellen.creditcard.onboarding.integration.*;
import com.ellen.creditcard.onboarding.integration.impl.*;
import com.ellen.creditcard.onboarding.repository.CreditCardApplicationRepository;
import com.ellen.creditcard.onboarding.service.mock.ThirdPartyMockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card onboarding service implementation testing cases
 */
@ExtendWith(MockitoExtension.class)
class CreditCardOnboardingServiceImplTest {
    @Mock
    private ThirdPartyMockService thirdPartyMockService;
    @Mock
    private CreditCardApplicationRepository applicationRepository;
    @InjectMocks
    private CreditCardOnboardingServiceImpl onboardingService;

    @Mock
    private IdentityServiceIntegrate identityServiceIntegrate;

    @Mock
    private BehaviorAnalysisServiceIntegrate behaviorAnalysisServiceIntegrate;

    @Mock
    private ComplianceInfoServiceIntegrate complianceInfoServiceIntegrate;

    @Mock
    private EmploymentProfileServiceIntegrate employmentProfileServiceIntegrate;

    @Mock
    private RiskEvaluationServiceIntegrate riskEvaluationServiceIntegrate;

    private CreditCardApplicationRequest request;
    private MultipartFile mockBankStatement;
    private CreditCardApplication application;

    @BeforeEach
    void setUp() {
        // Initialize mock bank statement file
        mockBankStatement = new MockMultipartFile("bankStatement", "statement.pdf", "application/pdf", "test data".getBytes());
        // Initialize mock application request
        request = new CreditCardApplicationRequest();
        request.setEmiratesId("7841990000000");
        request.setName("John Doe");
        request.setMobileNumber("0501234567");
        request.setIncome(new BigDecimal("150000"));
        request.setEmploymentDetails("Employed at XYZ Corp");
        request.setBankStatement(mockBankStatement);

        // Initialize mock application entity
        application = new CreditCardApplication();
        application.setId(1L);
        application.setEmiratesId(request.getEmiratesId());
        application.setName(request.getName());
        application.setIncome(request.getIncome());
        application.setEmploymentDetails(request.getEmploymentDetails());
        application.setBankStatementPath("uploads/12345_statement.pdf");
    }

    /**
     * Test: Reject application if identity verification fails (mandatory rule)
     */
    @Test
    void testSubmitApplication_IdentityVerificationFailed_Rejected() {
        // Mock: Identity verification returns NO
        when(identityServiceIntegrate.verifyIdentity(anyString())).thenReturn(CheckStatus.NO);
        when(applicationRepository.save(any(CreditCardApplication.class))).thenReturn(application);

        // Execute method
        var response = onboardingService.submitApplication(request);

        // Assertions
        assertEquals(0.0, response.getTotalScore());
        assertEquals(ApprovalStatus.REJECTED, response.getApprovalStatus());
        assertTrue(response.getApprovalComments().contains("Identity verification failed"));
        verify(thirdPartyMockService, never()).complianceBlacklistCheck(anyString(), anyString()); // No other checks executed
    }

    /**
     * Test: Approve with STP if all checks pass (100% total score)
     */
    @Test
    void testSubmitApplication_AllChecksPassed_STP() {
        // Mock: All checks pass, risk and behavioral scores are 100
        when(identityServiceIntegrate.verifyIdentity(anyString())).thenReturn(CheckStatus.YES);
        when(complianceInfoServiceIntegrate.complianceBlacklistCheck(anyString(), anyString())).thenReturn(CheckStatus.YES);
        when(employmentProfileServiceIntegrate.verifyEmployment(anyString())).thenReturn(CheckStatus.YES);
        when(riskEvaluationServiceIntegrate.evaluateCreditRisk(any(BigDecimal.class))).thenReturn(100.0);
        when(behaviorAnalysisServiceIntegrate.analyzeBehavior(any(MultipartFile.class))).thenReturn(100.0);
        when(applicationRepository.save(any(CreditCardApplication.class))).thenReturn(application);

        // Execute method
        var response = onboardingService.submitApplication(request);

        // Assertions: 100% score = STP
        assertEquals(100.0, response.getTotalScore());
        assertEquals(ApprovalStatus.STP, response.getApprovalStatus());
        assertTrue(response.getApprovalComments().contains("Card issued automatically"));
    }

    /**
     * Test: Approve with Near-STP for mixed scores (80% total score)
     */
    @Test
    void testSubmitApplication_MixedScores_NearSTP() {
        // Mock: Identity/Employment/Compliance pass, risk and behavioral scores are 50
        when(identityServiceIntegrate.verifyIdentity(anyString())).thenReturn(CheckStatus.YES);
        when(complianceInfoServiceIntegrate.complianceBlacklistCheck(anyString(), anyString())).thenReturn(CheckStatus.YES);
        when(employmentProfileServiceIntegrate.verifyEmployment(anyString())).thenReturn(CheckStatus.YES);
        when(riskEvaluationServiceIntegrate.evaluateCreditRisk(any(BigDecimal.class))).thenReturn(50.0);
        when(behaviorAnalysisServiceIntegrate.analyzeBehavior(any(MultipartFile.class))).thenReturn(50.0);
        when(applicationRepository.save(any(CreditCardApplication.class))).thenReturn(application);

        // Execute method
        var response = onboardingService.submitApplication(request);

        // Assertions: 80% score (20+20+20+10+10) = Near-STP
        assertEquals(80.0, response.getTotalScore());
        assertEquals(ApprovalStatus.NEAR_STP, response.getApprovalStatus());
        assertTrue(response.getApprovalComments().contains("credit limit to be set manually"));
    }

    /**
     * Test: Successfully query application result by ID
     */
    @Test
    void testGetApplicationResult_Success() {
        // Mock: Application found with 95% score and STP status
        application.setIdentityVerification(CheckStatus.YES);
        application.setTotalScore(95.0);
        application.setApprovalStatus(ApprovalStatus.STP);
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));

        // Execute method
        var response = onboardingService.getApplicationResult(1L);

        // Assertions
        assertEquals(1L, response.getApplicationId());
        assertEquals(95.0, response.getTotalScore());
        assertEquals(ApprovalStatus.STP, response.getApprovalStatus());
    }

    /**
     * Test: Throw exception if application ID is not found
     */
    @Test
    void testGetApplicationResult_NotFound() {
        // Mock: No application found for ID 999
        when(applicationRepository.findById(999L)).thenReturn(Optional.empty());

        // Assert exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> onboardingService.getApplicationResult(999L));
        assertTrue(exception.getMessage().contains("Application not found with ID - 999"));
    }
}
