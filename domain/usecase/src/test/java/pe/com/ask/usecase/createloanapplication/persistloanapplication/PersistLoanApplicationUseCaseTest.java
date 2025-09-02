package pe.com.ask.usecase.createloanapplication.persistloanapplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.usecase.utils.logmessages.CreateLoanApplicationUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersistLoanApplicationUseCaseTest {

    @Mock private LoanApplicationRepository loanApplicationRepository;
    @Mock private CustomLogger logger;
    private PersistLoanApplicationUseCase persistLoanApplicationUseCase;

    private UUID validUuid;
    private LoanApplication loanApplication;

    @BeforeEach
    void setUp() {
        persistLoanApplicationUseCase = new PersistLoanApplicationUseCase(loanApplicationRepository, logger);

        validUuid = UUID.randomUUID();

        loanApplication = new LoanApplication();
        loanApplication.setIdLoanApplication(validUuid);
    }

    @Test
    void execute_shouldReturnLoanApplication_whenCreationSucceeds() {
        when(loanApplicationRepository.createLoanApplication(loanApplication))
                .thenReturn(Mono.just(loanApplication));

        StepVerifier.create(persistLoanApplicationUseCase.execute(loanApplication))
                .expectNextMatches(app -> app.getIdLoanApplication().equals(validUuid))
                .verifyComplete();

        verify(logger, times(1))
                .trace(eq(CreateLoanApplicationUseCaseLog.LOAN_APPLICATION_CREATED), eq(validUuid));
    }

    @Test
    void execute_shouldReturnError_whenRepositoryFails() {
        when(loanApplicationRepository.createLoanApplication(loanApplication))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(persistLoanApplicationUseCase.execute(loanApplication))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger, never())
                .trace(eq(CreateLoanApplicationUseCaseLog.LOAN_APPLICATION_CREATED), any());
    }
}
