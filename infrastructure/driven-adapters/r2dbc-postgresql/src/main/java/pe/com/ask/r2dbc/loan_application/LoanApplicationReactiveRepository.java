package pe.com.ask.r2dbc.loan_application;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.ask.r2dbc.entity.LoanApplicationEntity;

import java.util.UUID;

public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, UUID>,
        ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

}