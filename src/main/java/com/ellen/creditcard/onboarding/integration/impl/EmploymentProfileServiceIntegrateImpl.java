package com.ellen.creditcard.onboarding.integration.impl;

import com.ellen.creditcard.onboarding.enums.CheckStatus;
import com.ellen.creditcard.onboarding.integration.EmploymentProfileServiceIntegrate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card employment profile verify third party service integrate implementation
 */
@Service
@Slf4j
public class EmploymentProfileServiceIntegrateImpl implements EmploymentProfileServiceIntegrate {

    /**
     * Employment Verification (Ministry of Labor API)
     * @param employmentDetails
     * @return
     */
    @Override
    public CheckStatus verifyEmployment(String employmentDetails) {
        log.info("Mock Labor Ministry Service: Verifying employment - {}", employmentDetails);
        // Employment Verification (Ministry of Labor API)
        return employmentDetails.toLowerCase().contains("employed") ? CheckStatus.YES : CheckStatus.NO;
    }
}
