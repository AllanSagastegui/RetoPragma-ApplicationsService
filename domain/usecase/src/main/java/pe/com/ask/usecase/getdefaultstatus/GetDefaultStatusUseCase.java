package pe.com.ask.usecase.getdefaultstatus;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.status.Status;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.exception.StatusNotFoundException;
import pe.com.ask.usecase.utils.STATUS_DEFAULT;
import pe.com.ask.usecase.utils.logmessages.LoanApplicationLog;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetDefaultStatusUseCase {

    private final StatusRepository statusRepository;
    private final CustomLogger logger;

    public Mono<Status> execute() {
        return statusRepository.findByName(STATUS_DEFAULT.PENDING_REVIEW.getName())
                .switchIfEmpty(Mono.defer(() -> {
                    logger.trace(LoanApplicationLog.STATUS_NOT_FOUND, STATUS_DEFAULT.PENDING_REVIEW.getName());
                    return Mono.error(new StatusNotFoundException());
                }));
    }
}