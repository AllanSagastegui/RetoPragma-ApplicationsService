package pe.com.ask.model.loanapplication.gateways;

import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CalculateCapacitySQSGateway {
    Mono<Void> publishDecision(
            ClientSnapshot clientSnapshot,
            List<LoanApplication>  loanApplicationData,
            LoanApplication loanApplication
    );
}