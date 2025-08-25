package pe.com.ask.r2dbc.status;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.com.ask.model.status.Status;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.r2dbc.entity.StatusEntity;
import pe.com.ask.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class StatusReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Status/* change for domain model */,
        StatusEntity/* change for adapter model */,
        UUID,
        StatusReactiveRepository
> implements StatusRepository {
    public StatusReactiveRepositoryAdapter(StatusReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Status.class/* change for domain model */));
    }

    @Override
    public Mono<Status> findByName(String name) {
        return super.repository.findByName(name);
    }
}
