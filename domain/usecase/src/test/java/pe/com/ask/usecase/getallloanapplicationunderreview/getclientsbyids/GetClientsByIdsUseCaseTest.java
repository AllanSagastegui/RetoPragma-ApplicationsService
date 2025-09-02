package pe.com.ask.usecase.getallloanapplicationunderreview.getclientsbyids;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.gateways.ClientSnapshotRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetClientsByIdsUseCaseTest {

    @Mock private ClientSnapshotRepository clientSnapshotRepository;
    @Mock private CustomLogger logger;

    private GetClientsByIdsUseCase useCase;

    private ClientSnapshot client1;
    private ClientSnapshot client2;
    private List<UUID> userIds;

    @BeforeEach
    void setUp() {
        useCase = new GetClientsByIdsUseCase(clientSnapshotRepository, logger);

        client1 = ClientSnapshot.builder()
                .id(UUID.randomUUID())
                .name("Alice")
                .dni("12345678")
                .build();

        client2 = ClientSnapshot.builder()
                .id(UUID.randomUUID())
                .name("Bob")
                .dni("87654321")
                .build();

        userIds = List.of(client1.getId(), client2.getId());
    }

    @Test
    @DisplayName("Should return map of clients when clients exist")
    void execute_shouldReturnMapOfClients_whenClientsExist() {
        when(clientSnapshotRepository.findClientsByIds(userIds)).thenReturn(Flux.just(client1, client2));

        StepVerifier.create(useCase.execute(userIds))
                .expectNextMatches(map -> map.size() == 2 &&
                        map.containsKey(client1.getId()) &&
                        map.containsKey(client2.getId()) &&
                        map.get(client1.getId()).equals(client1) &&
                        map.get(client2.getId()).equals(client2))
                .verifyComplete();

        verify(logger, times(1)).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_CLIENTS_BY_IDS_EXECUTING), eq(userIds));
        verify(logger, times(1)).trace(
                eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_CLIENTS_BY_IDS_RETRIEVED),
                eq(Set.of(client1.getId(), client2.getId()))
        );
    }

    @Test
    @DisplayName("Should return error when repository fails")
    void execute_shouldReturnError_whenRepositoryFails() {
        when(clientSnapshotRepository.findClientsByIds(userIds))
                .thenReturn(Flux.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(userIds))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_CLIENTS_BY_IDS_EXECUTING, userIds);
        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_CLIENTS_BY_IDS_ERROR, "DB error");
    }
}