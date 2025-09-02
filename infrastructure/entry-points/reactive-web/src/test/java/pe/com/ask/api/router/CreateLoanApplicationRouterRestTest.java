package pe.com.ask.api.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseCreateLoanApplication;
import pe.com.ask.api.exception.GlobalWebExceptionHandler;
import pe.com.ask.api.exception.model.ValidationException;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.handlers.CreateLoanApplicationHandler;
import pe.com.ask.api.mapper.LoanApplicationMapper;
import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.usecase.createloanapplication.CreateLoanApplicationUseCase;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {
        CreateLoanApplicationRouterRest.class,
        CreateLoanApplicationHandler.class,
        GlobalWebExceptionHandler.class,
        CreateLoanApplicationRouterRestTest.SecurityOverride.class
})
@WebFluxTest
class CreateLoanApplicationRouterRestTest {

    @Autowired private WebTestClient webTestClient;
    @MockitoBean private CreateLoanApplicationUseCase createLoanApplicationUseCase;
    @MockitoBean private LoanApplicationMapper loanApplicationMapper;
    @MockitoBean private ValidationService validationService;
    @MockitoBean private CustomLogger customLogger;

    private CreateLoanApplicationDTO requestDTO;
    private ResponseCreateLoanApplication responseDTO;

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
        LoanApplication loanApplication = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(new BigDecimal("2500"))
                .term(12)
                .email("test@test.com")
                .dni("12345678")
                .idStatus(UUID.randomUUID())
                .idLoanType(UUID.randomUUID())
                .build();

        requestDTO = new CreateLoanApplicationDTO(
                loanApplication.getAmount(),
                loanApplication.getTerm(),
                loanApplication.getEmail(),
                loanApplication.getDni(),
                "Personal Loan"
        );

        responseDTO = new ResponseCreateLoanApplication(
                UUID.randomUUID(),
                loanApplication.getAmount(),
                loanApplication.getTerm(),
                loanApplication.getEmail(),
                loanApplication.getDni(),
                "PENDING",
                "Personal Loan"
        );

        LoanApplicationData dto = LoanApplicationData.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(new BigDecimal("2500"))
                .term(12)
                .email("test@test.com")
                .dni("12345678")
                .status("PENDING")
                .loanType("Personal Loan")
                .build();

        when(validationService.validate(any(CreateLoanApplicationDTO.class)))
                .thenReturn(Mono.just(requestDTO));
        when(loanApplicationMapper.toDomain(any(CreateLoanApplicationDTO.class)))
                .thenReturn(loanApplication);
        when(createLoanApplicationUseCase.createLoanApplication(any(LoanApplication.class), any(String.class)))
                .thenReturn(Mono.just(dto));
        when(loanApplicationMapper.toResponse(any(LoanApplicationData.class)))
                .thenReturn(responseDTO);
    }

    @Test
    void testCreateLoanApplication_Returns201() {
        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseCreateLoanApplication.class)
                .isEqualTo(responseDTO);
    }

    @Test
    void testCreateLoanApplication_Returns400_WhenValidationFails() {
        ValidationException ex = new ValidationException(
                Map.of("term", "Term cannot be less than 0")
        );

        when(validationService.validate(any(CreateLoanApplicationDTO.class)))
                .thenReturn(Mono.error(ex));

        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isEqualTo(ex.getStatus())
                .expectBody()
                .jsonPath("$.errors.term").isEqualTo("Term cannot be less than 0");
    }

    @Test
    void testCreateLoanApplication_Returns500_WhenUnexpectedError() {
        when(createLoanApplicationUseCase.createLoanApplication(any(), any()))
                .thenReturn(Mono.error(new RuntimeException("Unexpected")));

        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.status").isEqualTo("500");
    }

    @Test
    void testHandle_BaseExceptionWithNullTimestamp() throws JsonProcessingException {
        BaseException ex = new BaseException("CODE", "TITLE", "MESSAGE", 400, Map.of()) {
            @Override
            public LocalDateTime getTimestamp() {
                return null;
            }
        };

        when(validationService.validate(any(CreateLoanApplicationDTO.class)))
                .thenReturn(Mono.error(ex));

        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody()
                .jsonPath("$.timestamp").exists();
    }

    @Test
    void testHandle_JsonProcessingException() {
        BaseException ex = new BaseException("CODE", "TITLE", "MESSAGE", 400, Map.of()) {
            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.now();
            }
        };

        GlobalWebExceptionHandler handler = new GlobalWebExceptionHandler(new ObjectMapper() {
            @Override
            public byte[] writeValueAsBytes(Object value) throws JsonProcessingException {
                throw new JsonProcessingException("forced") {};
            }
        }, customLogger);

        WebTestClient client = WebTestClient.bindToWebHandler((exchange) ->
                handler.handle(exchange, ex)
        ).build();

        client.post()
                .uri("/api/v1/solicitud")
                .exchange()
                .expectStatus().is5xxServerError();
    }

}
