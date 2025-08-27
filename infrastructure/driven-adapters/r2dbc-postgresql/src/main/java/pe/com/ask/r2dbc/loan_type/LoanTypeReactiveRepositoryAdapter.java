package pe.com.ask.r2dbc.loan_type;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.r2dbc.entity.LoanTypeEntity;
import pe.com.ask.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType/* change for domain model */,
        LoanTypeEntity/* change for adapter model */,
        UUID,
        LoanTypeReactiveRepository
> implements LoanTypeRepository {
    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanType.class/* change for domain model */));
    }

    @Override
    public Mono<LoanType> findByName(String name) {
        return super.repository.findByName(name)
                .map(entity -> mapper.map(entity, LoanType.class));
    }
}
