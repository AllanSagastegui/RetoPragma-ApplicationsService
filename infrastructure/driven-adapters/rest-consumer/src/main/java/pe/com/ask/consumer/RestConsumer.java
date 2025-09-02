package pe.com.ask.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.ask.consumer.utils.routes.Routes;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.gateways.ClientSnapshotRepository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ClientSnapshotRepository {

    private final WebClient webClient;
    private final CustomLogger logger;

    @Override
    public Flux<ClientSnapshot> findClientsByIds(List<UUID> userIds) {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> {
                    var auth = ctx.getAuthentication();
                    if (auth.getCredentials() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
                        return jwt.getTokenValue();
                    }
                    return auth.getCredentials().toString();
                })
                .flatMapMany(token -> {
                    return webClient
                            .post()
                            .uri(Routes.GET_CLIENTS_BY_IDS)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .bodyValue(userIds)
                            .retrieve()
                            .bodyToFlux(ClientSnapshot.class);
                });
    }
}