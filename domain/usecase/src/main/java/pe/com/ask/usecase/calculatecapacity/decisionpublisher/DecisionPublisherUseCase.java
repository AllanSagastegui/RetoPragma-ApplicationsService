package pe.com.ask.usecase.calculatecapacity.decisionpublisher;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.CalculateCapacitySQSGateway;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.usecase.utils.logmessages.CalculateCapacityUseCaseLog;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DecisionPublisherUseCase {

    private final CalculateCapacitySQSGateway calculateCapacitySQSGateway;
    private final CustomLogger logger;

    public Mono<Void> publish(UUID loanApplicationId,
                              List<LoanApplication> approvedLoans,
                              ClientSnapshot clientSnapshot,
                              LoanApplication loanApplication) {

        logger.trace(CalculateCapacityUseCaseLog.PUBLISHING_DECISION,
                loanApplicationId,
                approvedLoans.size(),
                clientSnapshot != null);

        return calculateCapacitySQSGateway.publishDecision(clientSnapshot, approvedLoans, loanApplication);
    }
}