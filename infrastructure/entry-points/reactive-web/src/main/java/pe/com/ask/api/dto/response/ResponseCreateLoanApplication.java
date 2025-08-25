package pe.com.ask.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "ResponseCreateLoanApplication", description = "Response returned after creating a loan application")
public record ResponseCreateLoanApplication(
        @Schema(description = "Unique transaction ID for the loan application", example = "550e...")
        UUID idTransaction,

        @Schema(description = "Requested loan amount", example = "50000.00")
        BigDecimal amount,

        @Schema(description = "Loan term in months", example = "12")
        int term,

        @Schema(description = "Email of the applicant", example = "allan.sagastegui@test.com")
        String email,

        @Schema(description = "National ID (DNI) of the applicant", example = "12345678")
        String dni,

        @Schema(description = "Current status of the loan application")
        String status,

        @Schema(description = "Type of the loan")
        String loanType
) { }