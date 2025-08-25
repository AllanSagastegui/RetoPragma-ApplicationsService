package pe.com.ask.model.gateways;

import reactor.core.publisher.Mono;

public interface TransactionalGateway {
    <T>Mono<T> executeInTransaction(Mono<T> action);
}
