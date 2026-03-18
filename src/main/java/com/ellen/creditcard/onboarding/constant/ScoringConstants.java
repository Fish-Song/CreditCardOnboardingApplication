package com.ellen.creditcard.onboarding.constant;


/**
 * @author ellen
 * @date 2026/03/18
 * Centralized constants for credit card application scoring and approval rules
 * Easy to maintain and update without modifying business logic
 */
public final class ScoringConstants {
    // Private constructor to prevent instantiation
    private ScoringConstants() {}

    // ====================== Score Weights ======================
    public static final double IDENTITY_SCORE_WEIGHT = 20.0;
    public static final double EMPLOYMENT_SCORE_WEIGHT = 20.0;
    public static final double COMPLIANCE_SCORE_WEIGHT = 20.0;
    public static final double RISK_SCORE_MAX_WEIGHT = 20.0;
    public static final double BEHAVIORAL_SCORE_MAX_WEIGHT = 20.0;

    // ====================== Score Normalization ======================
    public static final double SCORE_NORMALIZATION_FACTOR = 100.0;
    public static final int SCORE_ROUNDING_SCALE = 2;
    public static final double ROUNDING_MULTIPLIER = Math.pow(10, SCORE_ROUNDING_SCALE);

    // ====================== Approval Thresholds ======================
    public static final double STP_THRESHOLD = 90.0;
    public static final double NEAR_STP_LOWER_THRESHOLD = 75.0;
    public static final double NEAR_STP_UPPER_THRESHOLD = 90.0;
    public static final double MANUAL_REVIEW_LOWER_THRESHOLD = 50.0;
    public static final double MANUAL_REVIEW_UPPER_THRESHOLD = 75.0;
    public static final double REJECTION_THRESHOLD = 50.0;
}
