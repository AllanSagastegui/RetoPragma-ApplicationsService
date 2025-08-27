package pe.com.ask.r2dbc.loan_type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.r2dbc.entity.LoanTypeEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanTypeReactiveRepositoryAdapterTest {

    @InjectMocks
    LoanTypeReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    LoanTypeReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private LoanType domain;
    private LoanTypeEntity entity;

    @BeforeEach
    void setup() {
        domain = LoanType.builder()
                .idLoanType(UUID.randomUUID())
                .name("Personal Loan")
                .interestRate(new BigDecimal("12.5"))
                .build();

        entity = LoanTypeEntity.builder()
                .idLoanType(domain.getIdLoanType())
                .name(domain.getName())
                .interestRate(domain.getInterestRate())
                .build();
    }

    @Test
    @DisplayName("Should find loan type by name successfully")
    void testFindByName() {
        when(repository.findByName("Personal Loan")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, LoanType.class)).thenReturn(domain);

        StepVerifier.create(repositoryAdapter.findByName("Personal Loan"))
                .expectNextMatches(result ->
                        result.getIdLoanType().equals(domain.getIdLoanType()) &&
                                result.getName().equals(domain.getName()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail to find loan type when repository throws error")
    void testFindByNameShouldFail() {
        when(repository.findByName("Business Loan"))
                .thenReturn(Mono.error(new RuntimeException("LoanType not found")));

        StepVerifier.create(repositoryAdapter.findByName("Business Loan"))
                .expectErrorMessage("LoanType not found")
                .verify();
    }

    @Test
    @DisplayName("Should return empty when loan type does not exist")
    void testFindByNameEmpty() {
        when(repository.findByName("Unknown Loan")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findByName("Unknown Loan"))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Should fail when loan type name is null")
    void testFindByNameNull() {
        when(repository.findByName(isNull()))
                .thenReturn(Mono.error(new RuntimeException("Name is null")));

        StepVerifier.create(repositoryAdapter.findByName(null))
                .expectErrorMessage("Name is null")
                .verify();
    }
}