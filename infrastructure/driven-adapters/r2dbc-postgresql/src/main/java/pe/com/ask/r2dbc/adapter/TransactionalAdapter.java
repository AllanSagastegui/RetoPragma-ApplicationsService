package pe.com.ask.r2dbc.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import pe.com.ask.model.gateways.TransactionalGateway;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionalAdapter implements TransactionalGateway {

    private final TransactionalOperator transactionalOperator;

    @Override
    public <T> Mono<T> executeInTransaction(Mono<T> action) {
        return transactionalOperator.transactional(action);
    }
}