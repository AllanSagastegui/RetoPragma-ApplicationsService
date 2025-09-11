package pe.com.ask.usecase.updateloanapplication;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.gateways.PublishDecisionSQSGateway;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.usecase.updateloanapplication.buildloanwithclient.BuildLoanWithClientUseCase;
import pe.com.ask.usecase.updateloanapplication.getclientandloantype.GetClientAndLoanTypeUseCase;
import pe.com.ask.usecase.updateloanapplication.getstatus.GetStatusUseCase;
import pe.com.ask.usecase.updateloanapplication.updateloan.UpdateLoanUseCase;
import pe.com.ask.usecase.utils.logmessages.UpdateLoanApplicationUseCaseLog;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class UpdateLoanApplicationUseCase {

    private final GetStatusUseCase getStatusUseCase;
    private final UpdateLoanUseCase  updateLoanUseCase;
    private final GetClientAndLoanTypeUseCase getClientAndLoanTypeUseCase;
    private final BuildLoanWithClientUseCase  buildLoanWithClientUseCase;
    private final PublishDecisionSQSGateway publishDecisionSQSGateway;
    private final CustomLogger logger;

    public Mono<LoanWithClient> execute(UUID idLoanApplication, String status) {
        logger.trace(UpdateLoanApplicationUseCaseLog.START_FLOW, idLoanApplication, status);

        return getStatusUseCase.execute(status)
                .flatMap(foundStatus ->
                        updateLoanUseCase.execute(idLoanApplication, foundStatus)
                                .flatMap(updatedLoanApplication ->
                                        getClientAndLoanTypeUseCase.execute(updatedLoanApplication)
                                                .flatMap(tuple -> {
                                                    logger.trace(UpdateLoanApplicationUseCaseLog.SUCCESS,
                                                            updatedLoanApplication.getIdLoanApplication(), status);
                                                    LoanWithClient loanWithClient = buildLoanWithClientUseCase.build(
                                                            updatedLoanApplication,
                                                            tuple.getT1(),
                                                            tuple.getT2(),
                                                            status
                                                    );

                                                    return publishDecisionSQSGateway.publishDecision(
                                                            loanWithClient
                                                    ).thenReturn(loanWithClient);
                                                })
                                )
                )
                .doOnError(err -> logger.trace(UpdateLoanApplicationUseCaseLog.ERROR_OCCURRED,
                        idLoanApplication, err.getMessage()));

    }
}