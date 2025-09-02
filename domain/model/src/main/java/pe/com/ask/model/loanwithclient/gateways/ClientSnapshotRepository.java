package pe.com.ask.model.loanwithclient.gateways;

import pe.com.ask.model.loanwithclient.ClientSnapshot;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface ClientSnapshotRepository {
    Flux<ClientSnapshot> findClientsByIds(List<UUID> userIds);
}