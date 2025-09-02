package pe.com.ask.r2dbc.status;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.ask.r2dbc.entity.StatusEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface StatusReactiveRepository extends ReactiveCrudRepository<StatusEntity, UUID>,
        ReactiveQueryByExampleExecutor<StatusEntity> {
    Mono<StatusEntity> findByName(String name);
    Flux<StatusEntity> findByNameIn(List<String> statusNames);

    @Query("""
        SELECT *
        FROM status
        WHERE LOWER(name) 
        LIKE LOWER(CONCAT('%', :namePart, '%'))
    """)
    Flux<StatusEntity> findByNameContaining(String namePart);
}