package com.ellen.creditcard.onboarding.integration;

import java.math.BigDecimal;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card risk evaluation third party service integrate
 */
public interface RiskEvaluationServiceIntegrate {

    /**
     * Evaluate each applicant’s financial history to
     * determine creditworthiness (using AECB data)
     * @param income
     * @return
     */
    Double evaluateCreditRisk(BigDecimal income);
}
