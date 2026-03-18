package com.ellen.creditcard.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card application request
 */

@Data
public class CreditCardApplicationRequest {
    @NotBlank(message = "Emirates ID is mandatory")
    private String emiratesId;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Mobile Number is mandatory")
    private String mobileNumber;
    private String nationality;
    private String address;
    @NotNull(message = "Annual Income is mandatory")
    private BigDecimal income;
    @NotBlank(message = "Employment Details is mandatory")
    private String employmentDetails;
    private BigDecimal requestedCreditLimit;
    @NotNull(message = "Bank Statement (6 months) is mandatory")
    private MultipartFile bankStatement; // 6-month bank statement file
}
