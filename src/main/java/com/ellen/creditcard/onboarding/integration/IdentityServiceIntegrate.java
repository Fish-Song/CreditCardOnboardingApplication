package com.ellen.creditcard.onboard.integration;

import org.springframework.stereotype.Component;

public interface IdentityServiceIntegrate {


    /**
     * Verification of Personal Details:
     * Ensure the accuracy and authenticity of applicant personal information. (EFR or ICA)
     *
     * @return verification result
     */
    Boolean verifyIdentity(String IdNumber);

}
