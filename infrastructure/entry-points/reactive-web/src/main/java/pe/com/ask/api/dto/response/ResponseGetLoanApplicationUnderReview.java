package pe.com.ask.api.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ResponseGetLoanApplicationUnderReview(
        String name,
        String email,
        BigDecimal amount,
        Integer loanTerm,
        BigDecimal baseSalary,
        String loanType,
        String loanStatus,

        BigDecimal interestRate,
        BigDecimal totalMonthlyDebt,
        Integer approvedLoans
) { }