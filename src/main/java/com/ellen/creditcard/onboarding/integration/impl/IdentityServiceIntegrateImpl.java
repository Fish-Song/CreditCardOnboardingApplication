package com.ellen.creditcard.onboarding.integration.impl;

import com.ellen.creditcard.onboarding.enums.CheckStatus;
import com.ellen.creditcard.onboarding.integration.IdentityServiceIntegrate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card identity verify third party service integrate implementation
 */
@Service
@Slf4j
public class IdentityServiceIntegrateImpl implements IdentityServiceIntegrate {

    /**
     * Identity Verification (ICA/Government Service) - Mandatory pass for further processing
     * @param emiratesId
     * @return
     */
    @Override
    public CheckStatus verifyIdentity(String emiratesId) {
        log.info("Mock ICA Service: Verifying identity for Emirates ID - {}", emiratesId);
        // Mock rule: Pass if Emirates ID is non-null/non-blank, else reject
        return emiratesId != null && !emiratesId.isBlank() ? CheckStatus.YES : CheckStatus.NO;
    }
}
