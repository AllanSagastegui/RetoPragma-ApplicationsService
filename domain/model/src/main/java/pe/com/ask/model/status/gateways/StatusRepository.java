package pe.com.ask.model.status.gateways;

import pe.com.ask.model.status.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface StatusRepository {

    Mono<Status> findByName(String name);
    Flux<UUID> findIdsByNames(List<String> statusNames);
    Flux<Status> findAll();
    Flux<Status> findByNameContaining(String namePart);
}