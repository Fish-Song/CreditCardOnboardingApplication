package com.ellen.creditcard.onboarding.integration;

import com.ellen.creditcard.onboarding.enums.CheckStatus;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card identity verify third party service integrate
 */
public interface IdentityServiceIntegrate {


    /**
     * Verification of Personal Details:
     * Ensure the accuracy and authenticity of applicant personal information. (EFR or ICA)
     *
     * @return verification result
     */
    public CheckStatus verifyIdentity(String emiratesId);

}
