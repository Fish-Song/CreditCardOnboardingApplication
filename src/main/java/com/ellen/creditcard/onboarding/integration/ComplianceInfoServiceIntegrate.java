package com.ellen.creditcard.onboarding.integration;

import com.ellen.creditcard.onboarding.enums.CheckStatus;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card compliance info check third party service integrate
 */
public interface ComplianceInfoServiceIntegrate {

    /**
     * Verify that applications comply with local and international financial regulations,
     * including anti-money laundering (AML) and know your customer (KYC) standards. (Blacklist Check)
     * @return
     */
    CheckStatus complianceBlacklistCheck(String emiratesId, String name);

}
