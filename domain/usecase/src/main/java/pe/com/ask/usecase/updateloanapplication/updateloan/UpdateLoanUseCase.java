package pe.com.ask.usecase.updateloanapplication.updateloan;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.status.Status;
import pe.com.ask.usecase.exception.LoanApplicationNotFoundException;
import pe.com.ask.usecase.utils.logmessages.UpdateLoanApplicationUseCaseLog;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class UpdateLoanUseCase {
    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomLogger logger;

    public Mono<LoanApplication> execute(UUID idLoanApplication, Status status) {
        return loanApplicationRepository.findLoanApplicationById(idLoanApplication)
                .doOnNext(app ->
                        logger.trace(UpdateLoanApplicationUseCaseLog.LOAN_APPLICATION_FOUND,
                                idLoanApplication, app.getIdStatus())
                )
                .switchIfEmpty(Mono.error(new LoanApplicationNotFoundException()))
                .flatMap(loanApplication -> {
                    loanApplication.setIdStatus(status.getIdStatus());
                    return loanApplicationRepository.updateLoanApplication(loanApplication)
                            .doOnNext(updated ->
                                    logger.trace(UpdateLoanApplicationUseCaseLog.STATUS_UPDATED,
                                            updated.getIdLoanApplication(), status.getName())
                            );
                });
    }
}