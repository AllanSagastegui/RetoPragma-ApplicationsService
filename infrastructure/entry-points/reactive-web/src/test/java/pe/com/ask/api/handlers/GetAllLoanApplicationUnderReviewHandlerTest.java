package pe.com.ask.api.handlers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.response.ResponseGetLoanApplicationUnderReview;
import pe.com.ask.api.mapper.LoanApplicationMapper;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.model.loanwithclient.Pageable;
import pe.com.ask.usecase.getallloanapplicationunderreview.GetAllLoanApplicationUnderReviewUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GetAllLoanApplicationUnderReviewHandlerTest {

    @Mock private LoanApplicationMapper mapper;
    @Mock private GetAllLoanApplicationUnderReviewUseCase getAllLoanApplicationUnderReviewUseCase;
    @Mock private ServerRequest serverRequest;
    @Mock private CustomLogger logger;

    private GetAllLoanApplicationUnderReviewHandler handler;
    private AutoCloseable mocks;

    private LoanWithClient loanWithClient;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        handler = new GetAllLoanApplicationUnderReviewHandler(mapper, logger, getAllLoanApplicationUnderReviewUseCase);

        ClientSnapshot client = ClientSnapshot.builder()
                .id(UUID.randomUUID())
                .name("John")
                .lastName("Doe")
                .dni("12345678")
                .email("john@example.com")
                .baseSalary(new BigDecimal("3000"))
                .build();

        LoanApplicationData loanData = LoanApplicationData.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(new BigDecimal("5000"))
                .term(12)
                .email(client.getEmail())
                .dni(client.getDni())
                .status("UNDER_REVIEW")
                .loanType("Personal Loan")
                .build();

        loanWithClient = LoanWithClient.builder()
                .clientSnapshot(client)
                .loanApplicationData(loanData)
                .interestRate(new BigDecimal("0.05"))
                .totalMonthlyDebt(new BigDecimal("500"))
                .approvedLoans(2)
                .build();

        Pageable<LoanWithClient> loanPage = Pageable.<LoanWithClient>builder()
                .page(0)
                .size(10)
                .totalElements(1L)
                .totalPages(1)
                .content(List.of(loanWithClient))
                .build();

        ResponseGetLoanApplicationUnderReview responseDto = new ResponseGetLoanApplicationUnderReview(
                client.getName(),
                client.getEmail(),
                loanData.getAmount(),
                loanData.getTerm(),
                client.getBaseSalary(),
                loanData.getLoanType(),
                loanData.getStatus(),
                loanWithClient.getInterestRate(),
                loanWithClient.getTotalMonthlyDebt(),
                loanWithClient.getApprovedLoans()
        );

        when(getAllLoanApplicationUnderReviewUseCase.execute(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.just(loanPage));
        when(mapper.toResponseGetLoanApplicationUnderReview(any(LoanWithClient.class)))
                .thenReturn(responseDto);
        when(serverRequest.queryParam("page")).thenReturn(java.util.Optional.of("0"));
        when(serverRequest.queryParam("size")).thenReturn(java.util.Optional.of("10"));
        when(serverRequest.queryParam("statusName")).thenReturn(java.util.Optional.of("UNDER_REVIEW"));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should handle GET /loans/under-review and return ServerResponse with loans")
    void testListenGETAllLoanApplicationUnderReviewUseCase() {
        Mono<ServerResponse> result = handler.listenGETAllLoanApplicationUnderReviewUseCase(serverRequest);

        StepVerifier.create(result)
                .expectNextMatches(ServerResponse.class::isInstance)
                .verifyComplete();

        verify(getAllLoanApplicationUnderReviewUseCase, times(1))
                .execute(0, 10, "UNDER_REVIEW");
        verify(mapper, times(1)).toResponseGetLoanApplicationUnderReview(loanWithClient);
    }
}
