package pe.com.ask.r2dbc.status;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.ask.model.status.Status;
import pe.com.ask.r2dbc.entity.StatusEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface StatusReactiveRepository extends ReactiveCrudRepository<StatusEntity, UUID>,
        ReactiveQueryByExampleExecutor<StatusEntity> {
    Mono<Status> findByName(String name);
}