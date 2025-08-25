package pe.com.ask.r2dbc.loan_aplication;

import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.r2dbc.entity.LoanApplicationEntity;
import pe.com.ask.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class LoanApplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanApplication/* change for domain model */,
        LoanApplicationEntity/* change for adapter model */,
        UUID,
        LoanApplicationReactiveRepository
> implements LoanApplicationRepository {
    public LoanApplicationReactiveRepositoryAdapter(LoanApplicationReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanApplication.class/* change for domain model */));
    }

    @Override
    public Mono<LoanApplication> createLoanApplication(LoanApplication loanApplication) {
        return super.save(loanApplication);
    }
}
