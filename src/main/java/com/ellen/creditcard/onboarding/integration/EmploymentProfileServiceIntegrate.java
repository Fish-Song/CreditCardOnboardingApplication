package com.ellen.creditcard.onboard.integration;

import org.springframework.stereotype.Component;

public interface EmploymentProfileServiceIntegrate {

    /**
     * Verify the employment details of credit card applicants to ensure they have a stable and reliable source of income,
     * which is a significant indicator of their ability to meet credit obligations.
     * @return verification result
     */
    Boolean employmentVerify();

}
