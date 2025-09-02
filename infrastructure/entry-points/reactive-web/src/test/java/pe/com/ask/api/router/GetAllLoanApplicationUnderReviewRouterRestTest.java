package pe.com.ask.api.router;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.ask.api.dto.response.ResponseGetLoanApplicationUnderReview;
import pe.com.ask.api.exception.GlobalWebExceptionHandler;
import pe.com.ask.api.handlers.GetAllLoanApplicationUnderReviewHandler;
import pe.com.ask.api.mapper.LoanApplicationMapper;
import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.model.loanwithclient.Pageable;
import pe.com.ask.usecase.getallloanapplicationunderreview.GetAllLoanApplicationUnderReviewUseCase;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {
        GetAllLoanApplicationUnderReviewRouterRest.class,
        GetAllLoanApplicationUnderReviewHandler.class,
        GlobalWebExceptionHandler.class,
        GetAllLoanApplicationUnderReviewRouterRestTest.SecurityOverride.class
})
@WebFluxTest
class GetAllLoanApplicationUnderReviewRouterRestTest {

    @Autowired private WebTestClient webTestClient;

    @MockitoBean private LoanApplicationMapper loanApplicationMapper;
    @MockitoBean private CustomLogger customLogger;
    @MockitoBean private GetAllLoanApplicationUnderReviewUseCase getAllLoanApplicationUnderReviewUseCase;

    @Configuration
    static class SecurityOverride {
        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
            return http
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange(auth -> auth
                            .anyExchange()
                            .permitAll()
                    )
                    .build();
        }
    }

    @BeforeEach
    void setUp() {
        LoanWithClient loanWithClient = LoanWithClient.builder()
                .loanApplicationData(
                        LoanApplicationData.builder()
                                .idLoanApplication(UUID.randomUUID())
                                .amount(BigDecimal.valueOf(1000))
                                .term(12)
                                .email("john.doe@test.com")
                                .dni("12345678")
                                .status("PENDING")
                                .loanType("Personal Loan")
                                .build()
                )
                .clientSnapshot(
                        ClientSnapshot.builder()
                                .id(UUID.randomUUID())
                                .name("John")
                                .lastName("Doe")
                                .dni("12345678")
                                .email("john.doe@test.com")
                                .baseSalary(BigDecimal.valueOf(2500))
                                .build()
                )
                .interestRate(BigDecimal.valueOf(0.05))
                .totalMonthlyDebt(BigDecimal.valueOf(300))
                .approvedLoans(2)
                .build();

        Pageable<LoanWithClient> pageResponse = Pageable.<LoanWithClient>builder()
                .page(0)
                .size(1)
                .totalElements(1L)
                .totalPages(1)
                .content(List.of(loanWithClient))
                .build();

        when(getAllLoanApplicationUnderReviewUseCase.execute(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.just(pageResponse));

        when(loanApplicationMapper.toResponseGetLoanApplicationUnderReview(loanWithClient))
                .thenReturn(new ResponseGetLoanApplicationUnderReview(
                        "John Doe",
                        "john.doe@test.com",
                        BigDecimal.valueOf(1000),
                        12,
                        BigDecimal.valueOf(2500),
                        "Personal Loan",
                        "PENDING",
                        BigDecimal.valueOf(0.05),
                        BigDecimal.valueOf(300),
                        2
                ));
    }


    @Test
    void testGetAllLoanApplicationUnderReview_Returns200AndBody() {
        webTestClient.get()
                .uri("/api/v1/solicitud?page=0&size=7&statusName=PENDING")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.page").isEqualTo(0)
                .jsonPath("$.size").isEqualTo(1)
                .jsonPath("$.totalElements").isEqualTo(1);
    }

    @Test
    void testGetAllLoanApplicationUnderReview_Returns500OnUnexpectedError() {
        when(getAllLoanApplicationUnderReviewUseCase.execute(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        webTestClient.get()
                .uri("/api/v1/solicitud?page=0&size=1&statusName=PENDING")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.status").isEqualTo("500")
                .jsonPath("$.message").exists();
    }

    @Test
    void testGetAllLoanApplicationUnderReview_ReturnsError_WhenBaseException() {
        BaseException ex = new BaseException("CODE", "TITLE", "MESSAGE", 400, null) {};
        when(getAllLoanApplicationUnderReviewUseCase.execute(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.error(ex));

        webTestClient.get()
                .uri("/api/v1/solicitud?page=0&size=1&statusName=PENDING")
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody()
                .jsonPath("$.errorCode").isEqualTo("CODE")
                .jsonPath("$.tittle").isEqualTo("TITLE")
                .jsonPath("$.message").isEqualTo("MESSAGE");
    }

    @Test
    void testGetAllLoanApplicationUnderReview_ReturnsError_WhenUnexpectedExceptionWrapped() {
        RuntimeException ex = new RuntimeException("Unexpected");
        when(getAllLoanApplicationUnderReviewUseCase.execute(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.error(ex));

        webTestClient.get()
                .uri("/api/v1/solicitud?page=0&size=1&statusName=PENDING")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.message").exists();
    }
}