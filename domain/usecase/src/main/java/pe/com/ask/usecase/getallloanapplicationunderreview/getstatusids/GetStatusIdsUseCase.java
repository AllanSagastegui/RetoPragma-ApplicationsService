package pe.com.ask.usecase.getallloanapplicationunderreview.getstatusids;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.status.Status;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetStatusIdsUseCase {

    private final StatusRepository statusRepository;
    private final CustomLogger logger;

    public Mono<List<UUID>> execute(String statusNameFilter) {
        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_EXECUTING, statusNameFilter);

        return Mono.justOrEmpty(statusNameFilter)
                .filter(s -> !s.isBlank())
                .flatMapMany(statusRepository::findByNameContaining)
                .map(Status::getIdStatus)
                .collectList()
                .flatMap(ids -> {
                    if (ids.isEmpty()) {
                        return statusRepository.findIdsByNames(List.of("Pendiente de revisión", "Rechazada"))
                                .collectList();
                    }
                    return Mono.just(ids);
                })
                .doOnNext(ids ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_RETRIEVED, ids))
                .doOnError(e ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_ERROR, e.getMessage()));
    }
}