package pe.com.ask.model.loantype.gateways;

import pe.com.ask.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> findByName(String name);
}