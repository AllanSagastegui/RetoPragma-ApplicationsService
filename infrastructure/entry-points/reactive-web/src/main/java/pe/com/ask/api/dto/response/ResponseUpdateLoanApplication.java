package pe.com.ask.api.dto.response;

import java.math.BigDecimal;

public record ResponseUpdateLoanApplication(
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
) {}