package pe.com.ask.model.loanapplication.gateways;

import pe.com.ask.model.loanwithclient.LoanWithClient;
import reactor.core.publisher.Mono;

public interface PublishDecisionSQSGateway {
    Mono<Void> publishDecision(LoanWithClient loanWithClient);
}