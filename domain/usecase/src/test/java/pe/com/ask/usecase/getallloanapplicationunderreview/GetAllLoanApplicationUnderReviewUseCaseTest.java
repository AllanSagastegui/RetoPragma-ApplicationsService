package pe.com.ask.usecase.getallloanapplicationunderreview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.model.loanwithclient.Pageable;
import pe.com.ask.usecase.getallloanapplicationunderreview.getallloantypes.GetAllLoanTypesUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getallstatusprocess.GetAllStatusProcessUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getclientsbyids.GetClientsByIdsUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getloansandtotal.GetLoansAndTotalUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getstatusids.GetStatusIdsUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.loanprocess.LoanProcessUseCase;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllLoanApplicationUnderReviewUseCaseTest {

    @Mock private GetStatusIdsUseCase getStatusIdsUseCase;
    @Mock private GetLoansAndTotalUseCase getLoansAndTotalUseCase;
    @Mock private GetAllStatusProcessUseCase getAllStatusProcessUseCase;
    @Mock private GetClientsByIdsUseCase getClientsByIdsUseCase;
    @Mock private GetAllLoanTypesUseCase getAllLoanTypesUseCase;
    @Mock private LoanProcessUseCase loanProcessUseCase;
    @Mock private CustomLogger logger;

    private GetAllLoanApplicationUnderReviewUseCase useCase;

    private LoanApplication loan1;
    private LoanApplication loan2;
    private UUID userId1;
    private UUID userId2;

    @BeforeEach
    void setUp() {
        useCase = new GetAllLoanApplicationUnderReviewUseCase(
                getStatusIdsUseCase,
                getLoansAndTotalUseCase,
                getAllStatusProcessUseCase,
                getClientsByIdsUseCase,
                getAllLoanTypesUseCase,
                loanProcessUseCase,
                logger
        );

        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();

        loan1 = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .userId(userId1)
                .idStatus(UUID.randomUUID())
                .idLoanType(UUID.randomUUID())
                .amount(BigDecimal.valueOf(1000))
                .term(12)
                .dni("12345678")
                .email("a@b.com")
                .build();

        loan2 = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .userId(userId2)
                .idStatus(UUID.randomUUID())
                .idLoanType(UUID.randomUUID())
                .amount(BigDecimal.valueOf(2000))
                .term(24)
                .dni("87654321")
                .email("x@y.com")
                .build();

        Map<UUID, Object> dummyClients = Map.of(
                userId1, new Object(),
                userId2, new Object()
        );

        Map<UUID, Object> dummyLoanTypes = Map.of(
                loan1.getIdLoanType(), new Object(),
                loan2.getIdLoanType(), new Object()
        );

        Map<UUID, String> statusMap = Map.of(
                loan1.getIdStatus(), "Pending",
                loan2.getIdStatus(), "Approved"
        );

        Tuple2<Long, List<LoanApplication>> loansAndTotal = Tuples.of(2L, List.of(loan1, loan2));

        lenient().when(getStatusIdsUseCase.execute("filter")).thenReturn(Mono.just(List.of(UUID.randomUUID())));
        lenient().when(getLoansAndTotalUseCase.execute(anyList(), anyInt(), anyInt())).thenReturn(Mono.just(loansAndTotal));
        lenient().when(getAllStatusProcessUseCase.execute()).thenReturn(Mono.just(statusMap));
        lenient().when(getClientsByIdsUseCase.execute(anyList()))
                .thenReturn((Mono) Mono.just(dummyClients));
        lenient().when(getAllLoanTypesUseCase.execute())
                .thenReturn((Mono) Mono.just(dummyLoanTypes));
        lenient().when(loanProcessUseCase.map(anyList(), anyMap(), anyMap(), anyMap()))
                .thenReturn(List.of(mock(LoanWithClient.class), mock(LoanWithClient.class)));
    }

    @Test
    @DisplayName("Should return pageable of LoanWithClient when data exists")
    void execute_shouldReturnPageable_whenDataExists() {
        StepVerifier.create(useCase.execute(0, 10, "filter"))
                .expectNextMatches(pageable ->
                        pageable.getContent().size() == 2 &&
                                pageable.getPage() == 0 &&
                                pageable.getSize() == 10 &&
                                pageable.getTotalElements() == 2L
                )
                .verifyComplete();

        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.START_USE_CASE), eq(0), eq(10), eq("filter"));
        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.STATUS_IDS_RETRIEVED), any());
        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.LOANS_AND_TOTAL_RETRIEVED), eq(2L), eq(2));
        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.USER_IDS_EXTRACTED), any());
        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.STATUS_MAP_RETRIEVED), anyInt());
        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.CLIENTS_RETRIEVED), anyInt());
        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.LOAN_TYPES_RETRIEVED), anyInt());
        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.LOANS_MAPPED), anyInt());
    }

    @Test
    @DisplayName("Should return error when any step fails")
    void execute_shouldReturnError_whenRepositoryFails() {
        when(getStatusIdsUseCase.execute("filter")).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(0, 10, "filter"))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.START_USE_CASE), eq(0), eq(10), eq("filter"));
        verify(logger).trace(eq(GetAllLoanApplicationUnderReviewUseCaseLog.ERROR_OCCURRED), eq("DB error"));
    }
}