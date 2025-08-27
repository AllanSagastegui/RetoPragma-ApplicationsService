package pe.com.ask.r2dbc.status;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.ask.r2dbc.entity.StatusEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface StatusReactiveRepository extends ReactiveCrudRepository<StatusEntity, UUID>,
        ReactiveQueryByExampleExecutor<StatusEntity> {
    Mono<StatusEntity> findByName(String name);
}