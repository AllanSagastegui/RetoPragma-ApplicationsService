package pe.com.ask.usecase.createloanapplication;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.model.status.Status;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.exception.LoanAmountOutOfRangeException;
import pe.com.ask.usecase.exception.LoanTypeNotFound;
import pe.com.ask.usecase.exception.StatusNotFound;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateLoanApplicationUseCaseTest {

    @Mock private LoanApplicationRepository loanApplicationRepository;
    @Mock private LoanTypeRepository loanTypeRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private TransactionalGateway transactionalGateway;
    @Mock private CustomLogger logger;

    private CreateLoanApplicationUseCase useCase;

    private LoanApplication loanApplication;
    private LoanType loanType;
    private Status status;

    @BeforeEach
    void setUp() {

        useCase = new CreateLoanApplicationUseCase(
                loanApplicationRepository,
                loanTypeRepository,
                statusRepository,
                transactionalGateway,
                logger
        );

        loanApplication = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(new BigDecimal("1000"))
                .term(12)
                .email("test@test.com")
                .dni("12345678")
                .build();

        loanType = LoanType.builder()
                .idLoanType(UUID.randomUUID())
                .name("Personal Loan")
                .minimumAmount(new BigDecimal("500"))
                .maximumAmount(new BigDecimal("5000"))
                .build();

        status = Status.builder()
                .idStatus(UUID.randomUUID())
                .name("Pendiente de revisión")
                .description("En proceso")
                .build();

        when(transactionalGateway.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("Should create LoanApplication successfully")
    void testCreateLoanApplicationSuccess() {
        when(loanTypeRepository.findByName("Personal Loan")).thenReturn(Mono.just(loanType));
        when(statusRepository.findByName("Pendiente de revisión")).thenReturn(Mono.just(status));
        when(loanApplicationRepository.createLoanApplication(any(LoanApplication.class)))
                .thenReturn(Mono.just(loanApplication));

        Mono<LoanApplicationData> result = useCase.createLoanApplication(loanApplication, "Personal Loan");

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getIdLoanApplication() != null &&
                                dto.getAmount().equals(loanApplication.getAmount()) &&
                                dto.getEmail().equals(loanApplication.getEmail()) &&
                                dto.getDni().equals(loanApplication.getDni()) &&
                                dto.getStatus().equals(status.getName()) &&
                                dto.getLoanType().equals(loanType.getName())
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw LoanTypeNotFound when loan type does not exist")
    void testLoanTypeNotFound() {
        when(loanTypeRepository.findByName("InvalidType")).thenReturn(Mono.empty());

        Mono<LoanApplicationData> result = useCase.createLoanApplication(loanApplication, "InvalidType");

        StepVerifier.create(result)
                .expectError(LoanTypeNotFound.class)
                .verify();
    }

    @Test
    @DisplayName("Should throw LoanAmountOutOfRangeException when amount is invalid")
    void testLoanAmountOutOfRange() {
        LoanType restrictedType = loanType.toBuilder()
                .minimumAmount(new BigDecimal("2000"))
                .maximumAmount(new BigDecimal("3000"))
                .build();

        when(loanTypeRepository.findByName("Personal Loan")).thenReturn(Mono.just(restrictedType));

        Mono<LoanApplicationData> result = useCase.createLoanApplication(loanApplication, "Personal Loan");

        StepVerifier.create(result)
                .expectError(LoanAmountOutOfRangeException.class)
                .verify();
    }

    @Test
    @DisplayName("Should throw StatusNotFound when status not found")
    void testStatusNotFound() {
        when(loanTypeRepository.findByName("Personal Loan")).thenReturn(Mono.just(loanType));
        when(statusRepository.findByName("Pendiente de revisión")).thenReturn(Mono.empty());

        Mono<LoanApplicationData> result = useCase.createLoanApplication(loanApplication, "Personal Loan");

        StepVerifier.create(result)
                .expectError(StatusNotFound.class)
                .verify();
    }
}