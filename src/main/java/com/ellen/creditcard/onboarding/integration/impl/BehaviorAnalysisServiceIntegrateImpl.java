package com.ellen.creditcard.onboarding.integration.impl;

import com.ellen.creditcard.onboarding.integration.BehaviorAnalysisServiceIntegrate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card behavior analysis third party service integrate implementation
 */

@Service
@Slf4j
public class BehaviorAnalysisServiceIntegrateImpl implements BehaviorAnalysisServiceIntegrate {

    private final Random random = new Random();

    /**
     * Behavioral Analysis (Internal LLM Service - File Exchange) - Score 0-100
     * @param bankStatement
     * @return
     */
    public Double analyzeBehavior(MultipartFile bankStatement) {
        log.info("Mock Behavioral Analysis Service: Analyzing bank statement - {}", bankStatement.getOriginalFilename());
        // Mock rule: Return 60-100 if file is non-null (random)
        return 60 + random.nextDouble() * 40;
    }

}
