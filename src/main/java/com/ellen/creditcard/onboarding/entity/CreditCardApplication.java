package com.ellen.creditcard.onboarding.entity;

import com.ellen.creditcard.onboarding.enums.ApprovalStatus;
import com.ellen.creditcard.onboarding.enums.CheckStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card application entity
 */

@Entity
@Table(name = "credit_card_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Applicant Basic Information
    @Column(nullable = false, unique = true)
    private String emiratesId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String mobileNumber;
    private String nationality;
    private String address;
    @Column(nullable = false)
    private BigDecimal income;
    @Column(nullable = false)
    private String employmentDetails;
    private BigDecimal requestedCreditLimit;
    private String bankStatementPath; // 银行流水文件存储路径

    // Third-Party Check Results
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CheckStatus identityVerification = CheckStatus.PENDING;
    @Enumerated(EnumType.STRING)
    private CheckStatus complianceCheck = CheckStatus.PENDING;
    @Enumerated(EnumType.STRING)
    private CheckStatus employmentVerification = CheckStatus.PENDING;
    private Double riskEvaluationScore; // 0-100
    private Double behavioralAnalysisScore; // 0-100

    // Third-Party Check Results
    private Double totalScore;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    private String approvalComments;
}