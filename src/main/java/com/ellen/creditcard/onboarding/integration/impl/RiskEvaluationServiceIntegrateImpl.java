package com.ellen.creditcard.onboarding.integration.impl;

import com.ellen.creditcard.onboarding.integration.RiskEvaluationServiceIntegrate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card risk evaluation third party service integrate implementation
 */
@Service
@Slf4j
public class RiskEvaluationServiceIntegrateImpl implements RiskEvaluationServiceIntegrate {

    private final Random random = new Random();

    /**
     *  Credit Risk Evaluation (AECB API) - Score 0-100
     * @param income
     * @return
     */
    @Override
    public Double evaluateCreditRisk(BigDecimal income) {
        log.info("Mock AECB Service: Evaluating credit risk for income - {}", income);
        // Mock rule: Higher income = higher score (50-100)
        double baseScore = income.compareTo(new BigDecimal("100000")) > 0 ? 90 : 60;
        return baseScore + random.nextDouble() * 10;
    }
}
