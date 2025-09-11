package pe.com.ask.model.loanapplication.gateways;

import pe.com.ask.model.loanapplication.LoanApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface LoanApplicationRepository {
    Mono<LoanApplication> createLoanApplication(LoanApplication loanApplication);
    Mono<LoanApplication> updateLoanApplication(LoanApplication loanApplication);
    Flux<LoanApplication> findLoansByIdStatus(List<UUID> statusIds, int offset, int limit);
    Mono<Long> countLoansByIdStatus(List<UUID> statusIds);
    Mono<LoanApplication> findLoanApplicationById(UUID id);
    Flux<LoanApplication> findAllApprovedLoansApplicationsByUserId(UUID userId);
}