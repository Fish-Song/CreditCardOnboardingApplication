package com.ellen.creditcard.onboarding.integration;

import com.ellen.creditcard.onboarding.enums.CheckStatus;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card employment profile verify third party service integrate
 */
public interface EmploymentProfileServiceIntegrate {

    /**
     * Verify the employment details of credit card applicants to ensure they have a stable and reliable source of income,
     * which is a significant indicator of their ability to meet credit obligations.
     * @return verification result
     */
    CheckStatus verifyEmployment(String employmentDetails);

}
