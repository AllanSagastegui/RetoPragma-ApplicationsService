package pe.com.ask.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(
        name = "ResponseUpdateLoanApplication",
        description = "Response returned after updating an existing loan application"
)
public record ResponseUpdateLoanApplication(
        @Schema(description = "Full name of the applicant", example = "Allan Sagastegui")
        String name,

        @Schema(description = "Email of the applicant", example = "allan.sagastegui@test.com")
        String email,

        @Schema(description = "Requested loan amount", example = "40000.00")
        BigDecimal amount,

        @Schema(description = "Loan term in months", example = "24")
        Integer loanTerm,

        @Schema(description = "Base salary of the applicant", example = "3200.00")
        BigDecimal baseSalary,

        @Schema(description = "Type of the loan", example = "Préstamo Vehicular")
        String loanType,

        @Schema(description = "Updated status of the loan application", example = "Aprobada")
        String loanStatus,

        @Schema(description = "Annual interest rate applied", example = "10.5")
        BigDecimal interestRate,

        @Schema(description = "Total monthly debt of the applicant", example = "1800.00")
        BigDecimal totalMonthlyDebt,

        @Schema(description = "Number of loans previously approved for the applicant", example = "3")
        Integer approvedLoans
) {}