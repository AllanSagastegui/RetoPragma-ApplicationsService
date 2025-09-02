package pe.com.ask.usecase.getallloanapplicationunderreview.getloansandtotal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GetLoansAndTotalUseCaseTest {

    @Mock private LoanApplicationRepository loanApplicationRepository;
    @Mock private CustomLogger logger;

    private GetLoansAndTotalUseCase useCase;

    private List<UUID> statusIds;
    private LoanApplication loan1;
    private LoanApplication loan2;

    @BeforeEach
    void setUp() {
        useCase = new GetLoansAndTotalUseCase(loanApplicationRepository, logger);

        statusIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        loan1 = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(new BigDecimal("1000"))
                .term(12)
                .dni("12345678")
                .email("test1@test.com")
                .build();

        loan2 = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(new BigDecimal("2000"))
                .term(24)
                .dni("87654321")
                .email("test2@test.com")
                .build();
    }

    @Test
    @DisplayName("Should return empty result when statusIds is empty")
    void execute_shouldReturnEmptyWhenStatusIdsEmpty() {
        StepVerifier.create(useCase.execute(List.of(), 0, 10))
                .expectNextMatches(tuple -> tuple.getT1() == 0L && tuple.getT2().isEmpty())
                .verifyComplete();

        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_EMPTY_STATUS_IDS);
    }

    @Test
    @DisplayName("Should return loans and total when repository returns data")
    void execute_shouldReturnLoansAndTotal_whenRepositoryReturnsData() {
        when(loanApplicationRepository.countLoansByIdStatus(statusIds)).thenReturn(Mono.just(2L));
        when(loanApplicationRepository.findLoansByIdStatus(statusIds, 0, 10)).thenReturn(Flux.just(loan1, loan2));

        StepVerifier.create(useCase.execute(statusIds, 0, 10))
                .expectNextMatches(tuple -> {
                    assertEquals(2L, tuple.getT1());
                    assertEquals(2, tuple.getT2().size());
                    return tuple.getT2().containsAll(List.of(loan1, loan2));
                })
                .verifyComplete();

        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_EXECUTING, statusIds, 0, 10);
        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_RETRIEVED, 2L, 2);
    }

    @Test
    @DisplayName("Should return error when repository fails")
    void execute_shouldReturnError_whenRepositoryFails() {
        when(loanApplicationRepository.countLoansByIdStatus(statusIds)).thenReturn(Mono.error(new RuntimeException("DB count error")));
        when(loanApplicationRepository.findLoansByIdStatus(statusIds, 0, 10)).thenReturn(Flux.error(new RuntimeException("DB find error")));

        StepVerifier.create(useCase.execute(statusIds, 0, 10))
                .expectErrorMessage("DB count error")
                .verify();

        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_EXECUTING, statusIds, 0, 10);
        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_ERROR, "DB count error", "DB count error");
    }

    @Test
    @DisplayName("Should return empty result when statusIds is null")
    void execute_shouldReturnEmptyWhenStatusIdsNull() {
        StepVerifier.create(useCase.execute(null, 0, 10))
                .expectNextMatches(tuple -> tuple.getT1() == 0L && tuple.getT2().isEmpty())
                .verifyComplete();

        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_EMPTY_STATUS_IDS);
    }

}