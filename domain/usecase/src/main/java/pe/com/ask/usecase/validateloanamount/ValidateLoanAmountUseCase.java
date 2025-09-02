package pe.com.ask.usecase.validateloanamount;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.usecase.exception.LoanAmountOutOfRangeException;
import pe.com.ask.usecase.utils.logmessages.LoanApplicationLog;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidateLoanAmountUseCase {

    private final CustomLogger logger;

    public Mono<LoanApplication> execute(LoanApplication loanApplication, LoanType loanType) {
        boolean validAmount = loanApplication.getAmount().compareTo(loanType.getMinimumAmount()) >= 0 &&
                loanApplication.getAmount().compareTo(loanType.getMaximumAmount()) <= 0;

        if (!validAmount) {
            logger.trace(LoanApplicationLog.LOAN_AMOUNT_OUT_OF_RANGE,
                    loanApplication.getAmount(), loanType.getName());
            return Mono.error(new LoanAmountOutOfRangeException());
        }

        loanApplication.setIdLoanType(loanType.getIdLoanType());
        return Mono.just(loanApplication);
    }
}