package pe.com.ask.r2dbc.loan_type;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.ask.r2dbc.entity.LoanTypeEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, UUID>,
        ReactiveQueryByExampleExecutor<LoanTypeEntity> {
    Mono<LoanTypeEntity> findByName(String name);
}