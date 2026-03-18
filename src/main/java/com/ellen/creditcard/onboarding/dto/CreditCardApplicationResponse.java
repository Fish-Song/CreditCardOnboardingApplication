package com.ellen.creditcard.onboarding.dto;

import com.ellen.creditcard.onboarding.enums.ApprovalStatus;
import com.ellen.creditcard.onboarding.enums.CheckStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card onboarding application response
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardApplicationResponse {
    private Long applicationId;
    private String emiratesId;
    private String name;
    private BigDecimal income;
    // Third-Party Check Results
    private CheckStatus identityVerification;
    private CheckStatus complianceCheck;
    private CheckStatus employmentVerification;
    private Double riskEvaluationScore;
    private Double behavioralAnalysisScore;
    // Approval Results
    private Double totalScore;
    private ApprovalStatus approvalStatus;
    private String approvalComments;
}
