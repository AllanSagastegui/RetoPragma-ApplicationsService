package pe.com.ask.usecase.updateloanapplication.getstatus;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.status.Status;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.exception.StatusNotFoundException;
import pe.com.ask.usecase.utils.logmessages.UpdateLoanApplicationUseCaseLog;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetStatusUseCase {

    private final StatusRepository statusRepository;
    private final CustomLogger logger;

    public Mono<Status> execute(String statusName) {
        return statusRepository.findByName(statusName)
                .doOnNext(foundStatus ->
                        logger.trace(UpdateLoanApplicationUseCaseLog.STATUS_FOUND,
                                statusName, foundStatus.getIdStatus())
                )
                .switchIfEmpty(Mono.error(new StatusNotFoundException()));
    }
}