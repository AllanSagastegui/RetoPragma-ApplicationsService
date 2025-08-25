package pe.com.ask.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.r2dbc.entity.LoanApplicationEntity;
import pe.com.ask.r2dbc.loan_aplication.LoanApplicationReactiveRepository;
import pe.com.ask.r2dbc.loan_aplication.LoanApplicationReactiveRepositoryAdapter;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanApplicationReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    LoanApplicationReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    LoanApplicationReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private LoanApplication domain;
    private LoanApplicationEntity entity;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(LoanApplicationReactiveRepository.class);
        mapper = Mockito.mock(ObjectMapper.class);

        repositoryAdapter = new LoanApplicationReactiveRepositoryAdapter(repository, mapper);

        domain = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@test.com")
                .dni("12345678")
                .build();

        entity = LoanApplicationEntity.builder()
                .idLoanApplication(domain.getIdLoanApplication())
                .amount(domain.getAmount())
                .term(domain.getTerm())
                .email(domain.getEmail())
                .dni(domain.getDni())
                .build();
    }

    @Test
    void testCreateLoanApplication() {
        when(mapper.map(domain, LoanApplicationEntity.class)).thenReturn(entity);
        when(mapper.map(entity, LoanApplication.class)).thenReturn(domain);
        when(repository.save(entity)).thenReturn(Mono.just(entity));

        System.out.println("=== Starting testCreateLoanApplication ===");
        System.out.println("Domain object before save: " + domain);

        StepVerifier.create(
                        repositoryAdapter.createLoanApplication(domain)
                                .doOnNext(result -> System.out.println("Result from createLoanApplication: " + result))
                )
                .expectNextMatches(result -> {
                    boolean matches = result.getIdLoanApplication().equals(domain.getIdLoanApplication()) &&
                            result.getAmount().equals(domain.getAmount()) &&
                            result.getTerm() == domain.getTerm() &&
                            result.getEmail().equals(domain.getEmail()) &&
                            result.getDni().equals(domain.getDni());

                    System.out.println("Match check: " + matches);
                    return matches;
                })
                .verifyComplete();

        System.out.println("=== Finished testCreateLoanApplication ===");
    }

}