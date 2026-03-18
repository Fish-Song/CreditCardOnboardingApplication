package com.ellen.creditcard.onboarding.service.mock;

import com.ellen.creditcard.onboarding.enums.CheckStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Mock implementation for all third-party external system integrations
 * Simulates ICA, Compliance, Labor Ministry, AECB, and Behavioral Analysis services
 */
@Service
@Slf4j
public class ThirdPartyMockService {
    private final Random random = new Random();

    // 1. Identity Verification (ICA/Government Service) - Mandatory pass for further processing
    public CheckStatus verifyIdentity(String emiratesId) {
        log.info("Mock ICA Service: Verifying identity for Emirates ID - {}", emiratesId);
        // Mock rule: Pass if Emirates ID is non-null/non-blank, else reject
        return emiratesId != null && !emiratesId.isBlank() ? CheckStatus.YES : CheckStatus.NO;
    }

    // 2. Compliance Check (Blacklist) - Third-party commercial product
    public CheckStatus complianceBlacklistCheck(String emiratesId, String name) {
        log.info("Mock Compliance Service: Blacklist check for {} - {}", name, emiratesId);
        // Mock rule: 90% pass rate (random)
        return random.nextDouble() < 0.9 ? CheckStatus.YES : CheckStatus.NO;
    }

    // 3. Employment Verification (Ministry of Labor API)
    public CheckStatus verifyEmployment(String employmentDetails) {
        log.info("Mock Labor Ministry Service: Verifying employment - {}", employmentDetails);
        // 3. Employment Verification (Ministry of Labor API)
        return employmentDetails.toLowerCase().contains("employed") ? CheckStatus.YES : CheckStatus.NO;
    }

    // 4. Credit Risk Evaluation (AECB API) - Score 0-100
    public Double evaluateCreditRisk(BigDecimal income) {
        log.info("Mock AECB Service: Evaluating credit risk for income - {}", income);
        // Mock rule: Higher income = higher score (50-100)
        double baseScore = income.compareTo(new BigDecimal("100000")) > 0 ? 90 : 60;
        return baseScore + random.nextDouble() * 10;
    }

    // 5. Behavioral Analysis (Internal LLM Service - File Exchange) - Score 0-100
    public Double analyzeBehavior(MultipartFile bankStatement) {
        log.info("Mock Behavioral Analysis Service: Analyzing bank statement - {}", bankStatement.getOriginalFilename());
        // Mock rule: Return 60-100 if file is non-null (random)
        return 60 + random.nextDouble() * 40;
    }
}
