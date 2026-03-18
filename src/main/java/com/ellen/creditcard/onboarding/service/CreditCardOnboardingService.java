package com.ellen.creditcard.onboarding.service;

import com.ellen.creditcard.onboarding.dto.CreditCardApplicationRequest;
import com.ellen.creditcard.onboarding.dto.CreditCardApplicationResponse;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card onboarding service
 */
public interface CreditCardOnboardingService {

    /**
     * Submit credit card application and complete full workflow (checks + scoring + approval)
     * @param request
     * @return
     */
    CreditCardApplicationResponse submitApplication(CreditCardApplicationRequest request);

    /**
     * Query approval result by application ID
     * @param applicationId
     * @return
     */
    CreditCardApplicationResponse getApplicationResult(Long applicationId);




}
