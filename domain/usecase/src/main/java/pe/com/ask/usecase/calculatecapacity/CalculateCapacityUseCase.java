package pe.com.ask.usecase.calculatecapacity;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.usecase.calculatecapacity.decisionpublisher.DecisionPublisherUseCase;
import pe.com.ask.usecase.calculatecapacity.getclient.GetClientUseCase;
import pe.com.ask.usecase.calculatecapacity.loantypevalidator.LoanTypeValidatorUseCase;
import pe.com.ask.usecase.utils.logmessages.CalculateCapacityUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CalculateCapacityUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final GetClientUseCase getClientUseCase;
    private final DecisionPublisherUseCase decisionPublisherUseCase;
    private final LoanTypeValidatorUseCase loanTypeValidatorUseCase;
    private final CustomLogger logger;

    public Mono<Void> execute(UUID loanApplicationId) {
        logger.trace(CalculateCapacityUseCaseLog.START_FLOW, loanApplicationId);

        return loanApplicationRepository.findLoanApplicationById(loanApplicationId)
                .flatMap(loanApplication ->
                        loanTypeRepository.findLoanTypeById(loanApplication.getIdLoanType())
                                .flatMap(loanTypeValidatorUseCase::validate)
                                .flatMap(validLoanType -> {
                                    UUID userId = loanApplication.getUserId();

                                    return getClientUseCase.fetch(userId)
                                            .flatMap((Tuple2<List<LoanApplication>, ?> tuple) -> {
                                                List<LoanApplication> approvedLoans = tuple.getT1();
                                                var clientSnapshot = (pe.com.ask.model.loanwithclient.ClientSnapshot) tuple.getT2();

                                                return decisionPublisherUseCase.publish(
                                                        loanApplicationId,
                                                        approvedLoans,
                                                        clientSnapshot,
                                                        loanApplication
                                                );
                                            });
                                })
                )
                .doOnSuccess(v -> logger.trace(CalculateCapacityUseCaseLog.SUCCESS, loanApplicationId))
                .doOnError(err -> logger.error(CalculateCapacityUseCaseLog.ERROR_OCCURRED, loanApplicationId, err.getMessage(), err))
                .onErrorResume(err -> Mono.empty());
    }
}