package pe.com.ask.model.loanapplication.gateways;

import pe.com.ask.model.loanapplication.LoanApplication;
import reactor.core.publisher.Mono;

public interface LoanApplicationRepository {
    Mono<LoanApplication> createLoanApplication(LoanApplication loanApplication);
}