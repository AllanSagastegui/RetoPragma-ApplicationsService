package pe.com.ask.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.ask.consumer.utils.routes.Routes;
import pe.com.ask.consumer.utils.token.Token;
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
        String token = Token.BEARER_TOKEN;
        logger.trace("Using fixed token: {}", token);
        return webClient
                .post()
                .uri(Routes.GET_CLIENTS_BY_IDS)
                .header(HttpHeaders.AUTHORIZATION, token)
                .bodyValue(userIds)
                .retrieve()
                .bodyToFlux(ClientSnapshot.class);
    }
}