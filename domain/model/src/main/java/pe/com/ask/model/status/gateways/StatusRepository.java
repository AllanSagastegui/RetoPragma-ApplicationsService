package pe.com.ask.model.status.gateways;

import pe.com.ask.model.status.Status;
import reactor.core.publisher.Mono;

public interface StatusRepository {
    Mono<Status> findByName(String name);
}