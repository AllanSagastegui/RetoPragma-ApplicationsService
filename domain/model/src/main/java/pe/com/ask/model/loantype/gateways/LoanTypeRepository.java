package pe.com.ask.model.loantype.gateways;

import pe.com.ask.model.loantype.LoanType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanTypeRepository {
    Mono<LoanType> findLoanTypeById(UUID id);
    Mono<LoanType> findByName(String name);
    Flux<LoanType> findAll();
}