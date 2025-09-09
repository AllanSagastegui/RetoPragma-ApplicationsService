package pe.com.ask.r2dbc.loan_application;

import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.r2dbc.entity.LoanApplicationEntity;
import pe.com.ask.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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

    @Override
    public Mono<LoanApplication> updateLoanApplication(LoanApplication loanApplication) {
        return super.save(loanApplication);
    }

    @Override
    public Flux<LoanApplication> findLoansByIdStatus(List<UUID> statusIds, int offset, int limit) {

        return repository.findAllByIdStatusIn(statusIds, offset, limit)
                .map(entity -> mapper.map(entity, LoanApplication.class));
    }

    @Override
    public Mono<Long> countLoansByIdStatus(List<UUID> statusIds) {
        return repository.countByIdStatusIn(statusIds);
    }

    @Override
    public Mono<LoanApplication> findLoanApplicationById(UUID id) {
        return super.findById(id);
    }
}
