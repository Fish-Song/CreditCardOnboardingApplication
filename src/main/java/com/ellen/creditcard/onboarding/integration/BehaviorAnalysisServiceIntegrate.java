package com.ellen.creditcard.onboard.integration;

import org.springframework.stereotype.Component;

public interface BehaviorAnalysisServiceIntegrate {

    /**
     * Implement an analysis of spending habits and payment history to
     * predict future credit behavior.
     * @param IdNumber
     * @return
     */
    Integer behaviorAnalysis(String IdNumber);
}
