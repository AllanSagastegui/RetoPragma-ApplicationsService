package pe.com.ask.usecase.createloanapplication.validateloantype;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.usecase.exception.LoanTypeNotFoundException;
import pe.com.ask.usecase.utils.logmessages.CreateLoanApplicationUseCaseLog;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidateLoanTypeUseCase {

    private final LoanTypeRepository  loanTypeRepository;
    private final CustomLogger logger;

    public Mono<LoanType> execute(String loanTypeName) {
        return loanTypeRepository.findByName(loanTypeName)
                .doOnNext(type -> logger.trace(CreateLoanApplicationUseCaseLog.LOAN_TYPE_FOUND, type.getName()))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.trace(CreateLoanApplicationUseCaseLog.LOAN_TYPE_NOT_FOUND, loanTypeName);
                    return Mono.error(new LoanTypeNotFoundException());
                }));
    }
}