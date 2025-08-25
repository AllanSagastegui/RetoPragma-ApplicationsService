package pe.com.ask.r2dbc.loan_aplication;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.ask.r2dbc.entity.LoanApplicationEntity;

import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, UUID>,
        ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

}