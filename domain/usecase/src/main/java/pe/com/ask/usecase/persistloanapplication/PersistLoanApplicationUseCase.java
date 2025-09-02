package pe.com.ask.usecase.persistloanapplication;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.usecase.utils.logmessages.LoanApplicationLog;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PersistLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomLogger logger;

    public Mono<LoanApplication> execute(LoanApplication loanApplication) {
        return loanApplicationRepository.createLoanApplication(loanApplication)
                .doOnSuccess(app -> logger.trace(LoanApplicationLog.LOAN_APPLICATION_CREATED, app.getIdLoanApplication()));
    }
}