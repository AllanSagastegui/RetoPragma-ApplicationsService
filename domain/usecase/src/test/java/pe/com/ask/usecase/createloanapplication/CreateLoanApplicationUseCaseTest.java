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
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loanwithclient.gateways.UserIdentityGateway;
import pe.com.ask.model.status.Status;
import pe.com.ask.usecase.exception.LoanAmountOutOfRangeException;
import pe.com.ask.usecase.exception.LoanTypeNotFoundException;
import pe.com.ask.usecase.exception.StatusNotFoundException;
import pe.com.ask.usecase.exception.UnauthorizedLoanApplicationException;
import pe.com.ask.usecase.createloanapplication.getdefaultstatus.GetDefaultStatusUseCase;
import pe.com.ask.usecase.createloanapplication.persistloanapplication.PersistLoanApplicationUseCase;
import pe.com.ask.usecase.createloanapplication.validateloanamount.ValidateLoanAmountUseCase;
import pe.com.ask.usecase.createloanapplication.validateloantype.ValidateLoanTypeUseCase;
import pe.com.ask.usecase.utils.logmessages.CreateLoanApplicationUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateLoanApplicationUseCaseTest {

    @Mock private ValidateLoanTypeUseCase validateLoanTypeUseCase;
    @Mock private ValidateLoanAmountUseCase validateLoanAmountUseCase;
    @Mock private GetDefaultStatusUseCase getDefaultStatusUseCase;
    @Mock private PersistLoanApplicationUseCase persistLoanApplicationUseCase;
    @Mock private TransactionalGateway transactionalGateway;
    @Mock private UserIdentityGateway userIdentityGateway;
    @Mock private CustomLogger logger;

    private CreateLoanApplicationUseCase useCase;
    private LoanApplication loanApplication;
    private LoanType loanType;
    private Status status;
    private UUID userId;
    private String dni;
    private String loanTypeName;

    @BeforeEach
    void setUp() {
        useCase = new CreateLoanApplicationUseCase(
                validateLoanTypeUseCase,
                validateLoanAmountUseCase,
                getDefaultStatusUseCase,
                persistLoanApplicationUseCase,
                transactionalGateway,
                userIdentityGateway,
                logger
        );

        dni = "12345678";
        userId = UUID.randomUUID();
        loanTypeName = "Personal Loan";

        loanApplication = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(new BigDecimal("1000"))
                .term(12)
                .email("test@test.com")
                .dni(dni)
                .build();

        loanType = LoanType.builder()
                .idLoanType(UUID.randomUUID())
                .name(loanTypeName)
                .minimumAmount(new BigDecimal("500"))
                .maximumAmount(new BigDecimal("5000"))
                .build();

        status = Status.builder()
                .idStatus(UUID.randomUUID())
                .name("Pendiente de revisión")
                .description("En proceso")
                .build();

        lenient().when(transactionalGateway.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("Should create LoanApplication successfully")
    void testCreateLoanApplicationSuccess() {
        when(userIdentityGateway.getCurrentUserDni()).thenReturn(Mono.just(dni));
        when(userIdentityGateway.getCurrentUserId()).thenReturn(Mono.just(userId.toString()));

        when(validateLoanTypeUseCase.execute(loanTypeName)).thenReturn(Mono.just(loanType));
        when(validateLoanAmountUseCase.execute(loanApplication, loanType)).thenReturn(Mono.just(loanApplication));
        when(getDefaultStatusUseCase.execute()).thenReturn(Mono.just(status));
        when(persistLoanApplicationUseCase.execute(loanApplication)).thenReturn(Mono.just(loanApplication));

        StepVerifier.create(useCase.createLoanApplication(loanApplication, loanTypeName))
                .expectNextMatches(dto ->
                        dto.getIdLoanApplication() != null &&
                                dto.getAmount().equals(loanApplication.getAmount()) &&
                                dto.getEmail().equals(loanApplication.getEmail()) &&
                                dto.getDni().equals(loanApplication.getDni()) &&
                                dto.getStatus().equals(status.getName()) &&
                                dto.getLoanType().equals(loanType.getName())
                )
                .verifyComplete();

        verify(logger, times(1)).trace(eq(CreateLoanApplicationUseCaseLog.START_FLOW), eq(dni));
        verify(logger, never()).trace(eq(CreateLoanApplicationUseCaseLog.DNI_MISMATCH), anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw UnauthorizedLoanApplicationException when DNI mismatches")
    void testDniMismatch() {
        when(userIdentityGateway.getCurrentUserDni()).thenReturn(Mono.just("other-dni"));
        when(userIdentityGateway.getCurrentUserId()).thenReturn(Mono.just(UUID.randomUUID().toString()));

        StepVerifier.create(useCase.createLoanApplication(loanApplication, loanTypeName))
                .expectError(UnauthorizedLoanApplicationException.class)
                .verify();

        verify(logger, times(1)).trace(eq(CreateLoanApplicationUseCaseLog.START_FLOW), eq(dni));
        verify(logger, times(1)).trace(eq(CreateLoanApplicationUseCaseLog.DNI_MISMATCH), eq(dni), eq("other-dni"));
    }

    @Test
    @DisplayName("Should throw LoanTypeNotFoundException when loan type does not exist")
    void testLoanTypeNotFound() {
        when(userIdentityGateway.getCurrentUserDni()).thenReturn(Mono.just(dni));
        when(userIdentityGateway.getCurrentUserId()).thenReturn(Mono.just(userId.toString()));

        when(validateLoanTypeUseCase.execute("InvalidType")).thenReturn(Mono.error(new LoanTypeNotFoundException()));

        StepVerifier.create(useCase.createLoanApplication(loanApplication, "InvalidType"))
                .expectError(LoanTypeNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should throw LoanAmountOutOfRangeException when amount is invalid")
    void testLoanAmountOutOfRange() {
        when(userIdentityGateway.getCurrentUserDni()).thenReturn(Mono.just(dni));
        when(userIdentityGateway.getCurrentUserId()).thenReturn(Mono.just(userId.toString()));

        LoanType restrictedType = loanType.toBuilder()
                .minimumAmount(new BigDecimal("2000"))
                .maximumAmount(new BigDecimal("3000"))
                .build();

        when(validateLoanTypeUseCase.execute(loanTypeName)).thenReturn(Mono.just(restrictedType));
        when(validateLoanAmountUseCase.execute(loanApplication, restrictedType))
                .thenReturn(Mono.error(new LoanAmountOutOfRangeException()));

        StepVerifier.create(useCase.createLoanApplication(loanApplication, loanTypeName))
                .expectError(LoanAmountOutOfRangeException.class)
                .verify();
    }

    @Test
    @DisplayName("Should throw StatusNotFoundException when status not found")
    void testStatusNotFound() {
        when(userIdentityGateway.getCurrentUserDni()).thenReturn(Mono.just(dni));
        when(userIdentityGateway.getCurrentUserId()).thenReturn(Mono.just(userId.toString()));

        when(validateLoanTypeUseCase.execute(loanTypeName)).thenReturn(Mono.just(loanType));
        when(validateLoanAmountUseCase.execute(loanApplication, loanType)).thenReturn(Mono.just(loanApplication));
        when(getDefaultStatusUseCase.execute()).thenReturn(Mono.error(new StatusNotFoundException()));

        StepVerifier.create(useCase.createLoanApplication(loanApplication, loanTypeName))
                .expectError(StatusNotFoundException.class)
                .verify();
    }
}