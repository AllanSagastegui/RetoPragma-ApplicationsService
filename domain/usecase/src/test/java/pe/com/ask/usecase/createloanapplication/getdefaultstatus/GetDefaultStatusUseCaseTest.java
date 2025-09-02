package pe.com.ask.usecase.createloanapplication.getdefaultstatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.status.Status;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.exception.StatusNotFoundException;
import pe.com.ask.usecase.utils.STATUS_DEFAULT;
import pe.com.ask.usecase.utils.logmessages.CreateLoanApplicationUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDefaultStatusUseCaseTest {

    @Mock private StatusRepository statusRepository;
    @Mock private CustomLogger logger;
    private GetDefaultStatusUseCase getDefaultStatusUseCase;

    @BeforeEach
    void setUp() {
        getDefaultStatusUseCase = new GetDefaultStatusUseCase(statusRepository, logger);
    }

    @Test
    void execute_shouldReturnStatus_whenStatusExists() {
        Status mockStatus = new Status();
        mockStatus.setName(STATUS_DEFAULT.PENDING_REVIEW.getName());

        when(statusRepository.findByName(anyString())).thenReturn(Mono.just(mockStatus));

        StepVerifier.create(getDefaultStatusUseCase.execute())
                .expectNextMatches(status -> status.getName().equals(STATUS_DEFAULT.PENDING_REVIEW.getName()))
                .verifyComplete();

        verify(logger, never()).trace(anyString(), any());
    }

    @Test
    void execute_shouldReturnError_whenStatusDoesNotExist() {
        when(statusRepository.findByName(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(getDefaultStatusUseCase.execute())
                .expectError(StatusNotFoundException.class)
                .verify();

        verify(logger, times(1))
                .trace(eq(CreateLoanApplicationUseCaseLog.STATUS_NOT_FOUND), eq(STATUS_DEFAULT.PENDING_REVIEW.getName()));
    }
}