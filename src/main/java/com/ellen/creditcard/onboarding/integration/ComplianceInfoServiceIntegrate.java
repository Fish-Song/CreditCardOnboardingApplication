package com.ellen.creditcard.onboard.integration;

import org.springframework.stereotype.Component;

public interface ComplianceInfoServiceIntegrate {

    /**
     * Verify that applications comply with local and international financial regulations,
     * including anti-money laundering (AML) and know your customer (KYC) standards. (Blacklist Check)
     * @return
     */
    Boolean complianceCheck();

}
