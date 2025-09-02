package pe.com.ask.usecase.getallloanapplicationunderreview.getstatusids;

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

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetStatusIdsUseCaseTest {

    @Mock private StatusRepository statusRepository;
    @Mock private CustomLogger logger;

    private GetStatusIdsUseCase useCase;

    private Status status1;
    private Status status2;

    @BeforeEach
    void setUp() {
        useCase = new GetStatusIdsUseCase(statusRepository, logger);

        status1 = Status.builder()
                .idStatus(UUID.randomUUID())
                .name("Pendiente de revisión")
                .build();

        status2 = Status.builder()
                .idStatus(UUID.randomUUID())
                .name("Rechazada")
                .build();
    }

    @Test
    @DisplayName("Should return status IDs when filter matches")
    void execute_shouldReturnStatusIds_whenFilterMatches() {
        when(statusRepository.findByNameContaining("Pendiente")).thenReturn(Flux.just(status1));

        StepVerifier.create(useCase.execute("Pendiente"))
                .expectNextMatches(ids -> ids.size() == 1 && ids.contains(status1.getIdStatus()))
                .verifyComplete();

        verify(logger, times(1)).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_EXECUTING), eq("Pendiente"));
        verify(logger, times(1)).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_RETRIEVED), anyList());
    }

    @Test
    @DisplayName("Should return default status IDs when filter matches nothing")
    void execute_shouldReturnDefaultStatusIds_whenFilterMatchesNothing() {
        when(statusRepository.findByNameContaining("NoMatch")).thenReturn(Flux.empty());
        when(statusRepository.findIdsByNames(List.of("Pendiente de revisión", "Rechazada")))
                .thenReturn(Flux.just(status1.getIdStatus(), status2.getIdStatus()));

        StepVerifier.create(useCase.execute("NoMatch"))
                .expectNextMatches(ids -> ids.size() == 2 &&
                        ids.contains(status1.getIdStatus()) &&
                        ids.contains(status2.getIdStatus()))
                .verifyComplete();

        verify(logger, times(1)).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_EXECUTING), eq("NoMatch"));
        verify(logger, times(1)).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_RETRIEVED), anyList());
    }

    @Test
    @DisplayName("Should return error when repository fails")
    void execute_shouldReturnError_whenRepositoryFails() {
        when(statusRepository.findByNameContaining("Pendiente"))
                .thenReturn(Flux.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute("Pendiente"))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger, times(1)).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_EXECUTING), eq("Pendiente"));
        verify(logger, times(1)).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_ERROR), eq("DB error"));
    }

    @Test
    @DisplayName("Should return default status IDs when filter is blank")
    void execute_shouldReturnDefaultStatusIds_whenFilterIsBlank() {
        when(statusRepository.findIdsByNames(List.of("Pendiente de revisión", "Rechazada")))
                .thenReturn(Flux.just(status1.getIdStatus(), status2.getIdStatus()));

        StepVerifier.create(useCase.execute("   "))
                .expectNextMatches(ids -> ids.size() == 2 &&
                        ids.contains(status1.getIdStatus()) &&
                        ids.contains(status2.getIdStatus()))
                .verifyComplete();

        verify(logger, times(1)).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_EXECUTING), eq("   "));
        verify(logger, times(1)).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.GET_STATUS_IDS_RETRIEVED), anyList());
    }

}