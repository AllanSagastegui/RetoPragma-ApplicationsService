package pe.com.ask.usecase.getallloanapplicationunderreview.getclientsbyids;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.gateways.ClientSnapshotRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GetClientsByIdsUseCase {
    private final ClientSnapshotRepository clientSnapshotRepository;
    private final CustomLogger logger;

    public Mono<Map<UUID, ClientSnapshot>> execute(List<UUID> userIds) {
        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_CLIENTS_BY_IDS_EXECUTING, userIds);

        return clientSnapshotRepository.findClientsByIds(userIds)
                .collectMap(ClientSnapshot::getId)
                .doOnNext(map ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_CLIENTS_BY_IDS_RETRIEVED, map.keySet()))
                .doOnError(e ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_CLIENTS_BY_IDS_ERROR, e.getMessage()));
    }
}