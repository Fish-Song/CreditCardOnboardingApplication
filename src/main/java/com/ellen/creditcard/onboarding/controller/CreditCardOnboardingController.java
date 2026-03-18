package com.ellen.creditcard.onboarding.controller;

import com.ellen.creditcard.onboarding.dto.CreditCardApplicationRequest;
import com.ellen.creditcard.onboarding.dto.CreditCardApplicationResponse;
import com.ellen.creditcard.onboarding.service.CreditCardOnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card onboarding controller
 */

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Tag(name = "Credit Card Onboarding API", description = "API for Credit Card Application, Verification, Scoring and Approval")
public class CreditCardOnboardingController {

    private final CreditCardOnboardingService onboardingService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Submit a new credit card application", description = "Submits application with bank statement, performs all third-party checks, scoring and approval")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Application submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - File upload/processing failed")
    })
    public ResponseEntity<CreditCardApplicationResponse> submitApplication(@Valid CreditCardApplicationRequest request) {
        CreditCardApplicationResponse response = onboardingService.submitApplication(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{applicationId}")
    @Operation(summary = "Get application approval result by ID", description = "Retrieves the complete check results, total score and approval status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Result retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<CreditCardApplicationResponse> getApplicationResult(@PathVariable Long applicationId) {
        CreditCardApplicationResponse response = onboardingService.getApplicationResult(applicationId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
