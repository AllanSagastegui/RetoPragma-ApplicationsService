package pe.com.ask.model.loanwithclient.gateways;

import reactor.core.publisher.Mono;

public interface UserIdentityGateway {
    Mono<String> getCurrentUserId();
    Mono<String> getCurrentUserDni();
}