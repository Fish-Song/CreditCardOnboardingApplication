package com.ellen.creditcard.onboarding.integration.impl;

import com.ellen.creditcard.onboarding.enums.CheckStatus;
import com.ellen.creditcard.onboarding.integration.ComplianceInfoServiceIntegrate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Random;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card compliance info check third party service integrate implementation
 */
@Service
@Slf4j
public class ComplianceInfoServiceIntegrateImpl implements ComplianceInfoServiceIntegrate {

    private final Random random = new Random();

    /**
     * Compliance Check (Blacklist) - Third-party commercial product
     * @param emiratesId
     * @param name
     * @return
     */
    @Override
    public CheckStatus complianceBlacklistCheck(String emiratesId, String name) {
        log.info("Mock Compliance Service: Blacklist check for {} - {}", name, emiratesId);
        // Mock rule: 90% pass rate (random)
        return random.nextDouble() < 0.9 ? CheckStatus.YES : CheckStatus.NO;
    }
}
