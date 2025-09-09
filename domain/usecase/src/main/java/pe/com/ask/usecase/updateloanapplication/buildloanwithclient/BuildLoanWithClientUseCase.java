package pe.com.ask.usecase.updateloanapplication.buildloanwithclient;

import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.LoanWithClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuildLoanWithClientUseCase {

    public LoanWithClient build(LoanApplication loanApplication,
                                ClientSnapshot clientSnapshot,
                                LoanType loanType,
                                String status) {

        LoanApplicationData loanApplicationData = LoanApplicationData.builder()
                .idLoanApplication(loanApplication.getIdLoanApplication())
                .amount(loanApplication.getAmount())
                .term(loanApplication.getTerm())
                .email(loanApplication.getEmail())
                .dni(loanApplication.getDni())
                .status(status)
                .loanType(loanType.getName())
                .build();

        BigDecimal monthlyPayment = calculateMonthlyPayment(
                loanApplication.getAmount(),
                loanType.getInterestRate(),
                loanApplication.getTerm()
        );

        return LoanWithClient.builder()
                .loanApplicationData(loanApplicationData)
                .clientSnapshot(clientSnapshot)
                .interestRate(loanType.getInterestRate())
                .totalMonthlyDebt(monthlyPayment)
                .approvedLoans(0)
                .build();
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRate, int termMonths) {
        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
        }
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal numerator = monthlyRate.multiply((monthlyRate.add(BigDecimal.ONE)).pow(termMonths));
        BigDecimal denominator = ((monthlyRate.add(BigDecimal.ONE)).pow(termMonths)).subtract(BigDecimal.ONE);

        return principal.multiply(numerator.divide(denominator, 10, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }
}