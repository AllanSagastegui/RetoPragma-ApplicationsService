package pe.com.ask.usecase.getallloanapplicationunderreview.loanprocess;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoanProcessUseCase {

    private final CustomLogger logger;

    public List<LoanWithClient> map(List<LoanApplication> loans,
                                    Map<UUID, ClientSnapshot> clients,
                                    Map<UUID, String> statuses,
                                    Map<UUID, LoanType> loanTypes) {

        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.LOAN_PROCESS_EXECUTING, loans.size());

        List<LoanWithClient> result = loans.stream().map(loan -> {
            LoanType loanType = loanTypes.get(loan.getIdLoanType());

            LoanApplicationData data = LoanApplicationData.builder()
                    .idLoanApplication(loan.getIdLoanApplication())
                    .amount(loan.getAmount())
                    .term(loan.getTerm())
                    .email(loan.getEmail())
                    .dni(loan.getDni())
                    .status(statuses.get(loan.getIdStatus()))
                    .loanType(loanType.getName())
                    .build();

            return LoanWithClient.builder()
                    .loanApplicationData(data)
                    .clientSnapshot(clients.get(loan.getUserId()))
                    .interestRate(loanType.getInterestRate())
                    .totalMonthlyDebt(calculateMonthlyPayment(
                            loan.getAmount(), loanType.getInterestRate(), loan.getTerm()))
                    .approvedLoans(0)
                    .build();
        }).collect(Collectors.toList());

        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.LOAN_PROCESS_COMPLETED, result.size());
        return result;
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