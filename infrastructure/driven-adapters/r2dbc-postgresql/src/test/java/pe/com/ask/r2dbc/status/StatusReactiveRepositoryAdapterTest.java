package pe.com.ask.r2dbc.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.com.ask.model.status.Status;
import pe.com.ask.r2dbc.entity.StatusEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatusReactiveRepositoryAdapterTest {

    @InjectMocks
    StatusReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    StatusReactiveRepository repository;

    @Mock ObjectMapper mapper;

    private Status domain;
    private StatusEntity entity;

    @BeforeEach
    void setup() {
        domain = Status.builder()
                .idStatus(UUID.randomUUID())
                .name("APPROVED")
                .description("Loan has been approved")
                .build();

        entity = StatusEntity.builder()
                .idStatus(domain.getIdStatus())
                .name(domain.getName())
                .description(domain.getDescription())
                .build();
    }

    @Test
    @DisplayName("Should find status by name successfully")
    void testFindByName() {
        when(repository.findByName("APPROVED")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Status.class)).thenReturn(domain);

        StepVerifier.create(repositoryAdapter.findByName("APPROVED"))
                .expectNextMatches(result ->
                        result.getIdStatus().equals(domain.getIdStatus()) &&
                                result.getName().equals("APPROVED"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail to find status when repository throws error")
    void testFindByNameShouldFail() {
        when(repository.findByName("REJECTED"))
                .thenReturn(Mono.error(new RuntimeException("Status not found")));

        StepVerifier.create(repositoryAdapter.findByName("REJECTED"))
                .expectErrorMessage("Status not found")
                .verify();
    }

    @Test
    @DisplayName("Should return empty when status does not exist")
    void testFindByNameEmpty() {
        when(repository.findByName("PENDING")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findByName("PENDING"))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Should fail when status name is null")
    void testFindByNameNull() {
        when(repository.findByName(isNull()))
                .thenReturn(Mono.error(new RuntimeException("Name is null")));

        StepVerifier.create(repositoryAdapter.findByName(null))
                .expectErrorMessage("Name is null")
                .verify();
    }
}