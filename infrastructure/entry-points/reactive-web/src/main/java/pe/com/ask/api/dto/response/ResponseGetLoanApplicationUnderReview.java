package pe.com.ask.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Schema(
        name = "ResponseGetLoanApplicationUnderReview",
        description = "Response returned when retrieving a loan application under review"
)
public record ResponseGetLoanApplicationUnderReview(
        @Schema(description = "Unique ID of the loan", example = "550e84...")
        UUID loanId,

        @Schema(description = "Full name of the applicant", example = "Allan Sagastegui")
        String name,

        @Schema(description = "Email of the applicant", example = "allan.sagastegui@test.com")
        String email,

        @Schema(description = "Requested loan amount", example = "50000.00")
        BigDecimal amount,

        @Schema(description = "Loan term in months", example = "12")
        Integer loanTerm,

        @Schema(description = "Base salary of the applicant", example = "3000.00")
        BigDecimal baseSalary,

        @Schema(description = "Type of the loan", example = "Préstamo Personal")
        String loanType,

        @Schema(description = "Current status of the loan application", example = "En revisión")
        String loanStatus,

        @Schema(description = "Annual interest rate applied", example = "12.5")
        BigDecimal interestRate,

        @Schema(description = "Total monthly debt of the applicant", example = "1500.00")
        BigDecimal totalMonthlyDebt,

        @Schema(description = "Number of loans previously approved for the applicant", example = "2")
        Integer approvedLoans
) { }