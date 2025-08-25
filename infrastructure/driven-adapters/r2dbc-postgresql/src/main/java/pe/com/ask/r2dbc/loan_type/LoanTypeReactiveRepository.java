package pe.com.ask.r2dbc.loan_type;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.r2dbc.entity.LoanTypeEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, UUID>,
        ReactiveQueryByExampleExecutor<LoanTypeEntity> {
    Mono<LoanType> findByName(String name);
}