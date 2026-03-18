package com.ellen.creditcard.onboarding.integration;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card behavior analysis third party service integrate
 */
public interface BehaviorAnalysisServiceIntegrate {

    /**
     * Implement an analysis of spending habits and payment history to
     * predict future credit behavior.
     * @param bankStatement
     * @return
     */
    Double analyzeBehavior(MultipartFile bankStatement);
}
