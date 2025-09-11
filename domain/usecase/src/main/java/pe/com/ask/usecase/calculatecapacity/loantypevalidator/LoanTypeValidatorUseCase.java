package pe.com.ask.usecase.calculatecapacity.loantypevalidator;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.usecase.utils.logmessages.CalculateCapacityUseCaseLog;
import pe.com.ask.model.gateways.CustomLogger;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanTypeValidatorUseCase {

    private final CustomLogger logger;

    public Mono<LoanType> validate(LoanType loanType) {
        logger.trace(CalculateCapacityUseCaseLog.LOAN_TYPE_FOUND,
                loanType.getIdLoanType(), loanType.getIdLoanType());

        if (!loanType.isAutomaticValidation()) {
            logger.trace(CalculateCapacityUseCaseLog.LOAN_TYPE_NO_AUTOMATIC,
                    loanType.getIdLoanType());
            return Mono.empty();
        }

        return Mono.just(loanType);
    }
}