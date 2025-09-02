package pe.com.ask.usecase.getallloanapplicationunderreview.getallstatusprocess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.status.Status;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllStatusProcessUseCaseTest {

    @Mock private StatusRepository statusRepository;
    @Mock private CustomLogger logger;

    private GetAllStatusProcessUseCase useCase;

    private Status status1;
    private Status status2;

    @BeforeEach
    void setUp() {
        useCase = new GetAllStatusProcessUseCase(statusRepository, logger);

        status1 = Status.builder()
                .idStatus(UUID.randomUUID())
                .name("Under Review")
                .description("Loan is being reviewed")
                .build();

        status2 = Status.builder()
                .idStatus(UUID.randomUUID())
                .name("Approved")
                .description("Loan has been approved")
                .build();
    }

    @Test
    @DisplayName("Should return map of status when statuses exist")
    void execute_shouldReturnMapOfStatuses_whenStatusesExist() {
        when(statusRepository.findAll()).thenReturn(Flux.just(status1, status2));

        StepVerifier.create(useCase.execute())
                .expectNextMatches(map -> map.size() == 2 &&
                        map.containsKey(status1.getIdStatus()) &&
                        map.containsKey(status2.getIdStatus()) &&
                        map.get(status1.getIdStatus()).equals(status1.getName()) &&
                        map.get(status2.getIdStatus()).equals(status2.getName()))
                .verifyComplete();

        verify(logger, times(1)).info(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_STATUS_PROCESS_EXECUTING);
        verify(logger, times(1)).info(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_STATUS_PROCESS_RETRIEVED,
                Map.of(status1.getIdStatus(), status1.getName(),
                        status2.getIdStatus(), status2.getName()));
        verify(logger, never()).error(anyString(), any());
    }

    @Test
    @DisplayName("Should return error when repository fails")
    void execute_shouldReturnError_whenRepositoryFails() {
        when(statusRepository.findAll()).thenReturn(Flux.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute())
                .expectErrorMessage("DB error")
                .verify();

        verify(logger, times(1)).info(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_STATUS_PROCESS_EXECUTING);
        verify(logger, times(1)).error(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_STATUS_PROCESS_ERROR, "DB error");
    }
}
