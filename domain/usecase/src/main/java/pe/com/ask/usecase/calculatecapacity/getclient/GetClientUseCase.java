package pe.com.ask.usecase.calculatecapacity.getclient;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.gateways.ClientSnapshotRepository;
import pe.com.ask.usecase.utils.logmessages.CalculateCapacityUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetClientUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ClientSnapshotRepository clientSnapshotRepository;
    private final CustomLogger logger;

    public Mono<Tuple2<List<LoanApplication>, ClientSnapshot>> fetch(UUID userId) {
        Mono<List<LoanApplication>> approvedLoansMono =
                loanApplicationRepository.findAllApprovedLoansApplicationsByUserId(userId)
                        .collectList();

        Mono<ClientSnapshot> clientMono =
                clientSnapshotRepository.findClientsByIds(List.of(userId))
                        .next()
                        .doOnSubscribe(s -> logger.trace(CalculateCapacityUseCaseLog.FETCHING_CLIENT, userId))
                        .doOnSuccess(c -> logger.trace(CalculateCapacityUseCaseLog.CLIENT_FOUND, userId, c != null));

        return Mono.zip(approvedLoansMono, clientMono);
    }
}