package pe.com.ask.r2dbc.loan_application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.r2dbc.entity.LoanApplicationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanApplicationReactiveRepositoryAdapterTest {

    @InjectMocks
    LoanApplicationReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    LoanApplicationReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private LoanApplication domain;
    private LoanApplicationEntity entity;

    @BeforeEach
    void setup() {
        domain = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@example.com")
                .dni("12345678")
                .idStatus(UUID.randomUUID())
                .idLoanType(UUID.randomUUID())
                .build();

        entity = LoanApplicationEntity.builder()
                .idLoanApplication(domain.getIdLoanApplication())
                .amount(domain.getAmount())
                .term(domain.getTerm())
                .email(domain.getEmail())
                .dni(domain.getDni())
                .idStatus(domain.getIdStatus())
                .idLoanType(domain.getIdLoanType())
                .build();
    }

    @Test
    @DisplayName("Should create loan application successfully")
    void testCreateLoanApplication() {
        when(mapper.map(domain, LoanApplicationEntity.class)).thenReturn(entity);
        when(mapper.map(entity, LoanApplication.class)).thenReturn(domain);
        when(repository.save(entity)).thenReturn(Mono.just(entity));

        StepVerifier.create(repositoryAdapter.createLoanApplication(domain))
                .expectNextMatches(result ->
                        result.getIdLoanApplication().equals(domain.getIdLoanApplication()) &&
                                result.getEmail().equals(domain.getEmail()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail to create loan application when repository throws error")
    void testCreateLoanApplicationShouldFail() {
        when(mapper.map(domain, LoanApplicationEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.error(new RuntimeException("Error creating loan application")));

        StepVerifier.create(repositoryAdapter.createLoanApplication(domain))
                .expectErrorMessage("Error creating loan application")
                .verify();
    }

    @Test
    @DisplayName("Should fail to create loan application when entity is null")
    void testCreateLoanApplicationShouldFailWithNull() {
        when(mapper.map(domain, LoanApplicationEntity.class)).thenReturn(null);
        when(repository.save(null)).thenReturn(Mono.error(new RuntimeException("Entity is null")));

        StepVerifier.create(repositoryAdapter.createLoanApplication(domain))
                .expectErrorMessage("Entity is null")
                .verify();
    }

    @Test
    @DisplayName("Should fail to create loan application when entity is empty")
    void testCreateLoanApplicationShouldFailWithEmptyValue() {
        when(mapper.map(domain, LoanApplicationEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.error(new RuntimeException("Entity is empty")));

        StepVerifier.create(repositoryAdapter.createLoanApplication(domain))
                .expectErrorMessage("Entity is empty")
                .verify();
    }
    @Test
    @DisplayName("Should find loans by status successfully")
    void testFindLoansByIdStatus() {
        List<UUID> statusIds = List.of(domain.getIdStatus());
        when(repository.findAllByIdStatusIn(statusIds, 0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, LoanApplication.class)).thenReturn(domain);

        StepVerifier.create(repositoryAdapter.findLoansByIdStatus(statusIds, 0, 10))
                .expectNextMatches(loan ->
                        loan.getIdLoanApplication().equals(domain.getIdLoanApplication()) &&
                                loan.getEmail().equals(domain.getEmail()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should count loans by status successfully")
    void testCountLoansByIdStatus() {
        List<UUID> statusIds = List.of(domain.getIdStatus());
        when(repository.countByIdStatusIn(statusIds)).thenReturn(Mono.just(5L));

        StepVerifier.create(repositoryAdapter.countLoansByIdStatus(statusIds))
                .expectNext(5L)
                .verifyComplete();
    }
}