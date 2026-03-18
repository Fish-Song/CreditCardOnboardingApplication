package com.ellen.creditcard.onboard.integration;


public interface RiskEvaluationServiceIntegrate {

    /**
     * Evaluate each applicant’s financial history to
     * determine creditworthiness (using AECB data)
     * @param IdNumber
     * @return
     */
    Integer riskEvaluation(String IdNumber);
}
