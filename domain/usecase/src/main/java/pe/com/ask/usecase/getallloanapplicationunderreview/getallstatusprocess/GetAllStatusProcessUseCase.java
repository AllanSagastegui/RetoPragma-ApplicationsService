package pe.com.ask.usecase.getallloanapplicationunderreview.getallstatusprocess;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.status.Status;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GetAllStatusProcessUseCase {

    private final StatusRepository statusRepository;
    private final CustomLogger logger;

    public Mono<Map<UUID, String>> execute() {
        logger.info(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_STATUS_PROCESS_EXECUTING);

        return statusRepository.findAll()
                .collectMap(Status::getIdStatus, Status::getName)
                .doOnNext(map ->
                        logger.info(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_STATUS_PROCESS_RETRIEVED, map))
                .doOnError(e -> logger.error(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_STATUS_PROCESS_ERROR, e.getMessage()));
    }
}
