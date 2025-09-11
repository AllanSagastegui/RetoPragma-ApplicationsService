package pe.com.ask.usecase.updateloanapplication.getclientandloantype;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.gateways.ClientSnapshotRepository;
import pe.com.ask.usecase.exception.ClientNotFoundException;
import pe.com.ask.usecase.exception.LoanTypeNotFoundException;
import pe.com.ask.usecase.utils.logmessages.UpdateLoanApplicationUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

@RequiredArgsConstructor
public class GetClientAndLoanTypeUseCase {
    private final ClientSnapshotRepository clientSnapshotRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final CustomLogger logger;

    public Mono<Tuple2<ClientSnapshot, LoanType>> execute(LoanApplication loanApplication) {
        return clientSnapshotRepository.findClientsByIds(List.of(loanApplication.getUserId()))
                .next()
                .doOnNext(client ->
                        logger.trace(UpdateLoanApplicationUseCaseLog.CLIENT_FOUND,
                                loanApplication.getUserId(), client.getDni())
                )
                .switchIfEmpty(Mono.defer(() -> {
                    logger.trace("Client not found for userId {}", loanApplication.getUserId());
                    return Mono.error(new ClientNotFoundException());
                }))
                .flatMap(clientSnapshot ->
                        loanTypeRepository.findLoanTypeById(loanApplication.getIdLoanType())
                                .doOnNext(loanType ->
                                        logger.trace(UpdateLoanApplicationUseCaseLog.LOAN_TYPE_FOUND,
                                                loanType.getIdLoanType(), loanType.getName())
                                )
                                .switchIfEmpty(Mono.error(new LoanTypeNotFoundException()))
                                .map(loanType -> Tuples.of(clientSnapshot, loanType))
                );
    }
}