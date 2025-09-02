package pe.com.ask.api.handlers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseCreateLoanApplication;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.LoanApplicationMapper;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.usecase.createloanapplication.CreateLoanApplicationUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateLoanApplicationHandlerTest {

    @Mock private LoanApplicationMapper mapper;
    @Mock private ValidationService validator;
    @Mock private CreateLoanApplicationUseCase createLoanApplicationUseCase;
    @Mock private ServerRequest serverRequest;
    @Mock private CustomLogger logger;

    private CreateLoanApplicationHandler handler;
    private AutoCloseable mocks;

    private CreateLoanApplicationDTO requestDto;
    private LoanApplication domain;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        requestDto = new CreateLoanApplicationDTO(
                new BigDecimal("10000.00"),
                12,
                "test@example.com",
                "12345678",
                "Préstamo Personal"
        );

        domain = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .amount(requestDto.amount())
                .term(requestDto.term())
                .email(requestDto.email())
                .dni(requestDto.dni())
                .build();

        LoanApplicationData domainData = new LoanApplicationData(
                domain.getIdLoanApplication(),
                domain.getAmount(),
                domain.getTerm(),
                domain.getEmail(),
                domain.getDni(),
                "PENDING",
                requestDto.loanType()
        );

        ResponseCreateLoanApplication response = new ResponseCreateLoanApplication(
                domainData.getIdLoanApplication(),
                domainData.getAmount(),
                domainData.getTerm(),
                domainData.getEmail(),
                domainData.getDni(),
                domainData.getStatus(),
                domainData.getLoanType()
        );

        when(serverRequest.bodyToMono(CreateLoanApplicationDTO.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(any(CreateLoanApplicationDTO.class))).thenReturn(Mono.just(requestDto));
        when(mapper.toDomain(requestDto)).thenReturn(domain);
        when(createLoanApplicationUseCase.createLoanApplication(domain, requestDto.loanType()))
                .thenReturn(Mono.just(domainData));
        when(mapper.toResponse(domainData)).thenReturn(response);

        handler = new CreateLoanApplicationHandler(mapper, validator, logger, createLoanApplicationUseCase);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should handle POST /loan and return ServerResponse with created LoanApplication")
    void testListenPOSTCreateLoanApplicationUseCase() {
        when(serverRequest.bodyToMono(CreateLoanApplicationDTO.class)).thenReturn(Mono.just(requestDto));

        Mono<ServerResponse> result = handler.listenPOSTCreateLoanApplicationUseCase(serverRequest);

        StepVerifier.create(result)
                .expectNextMatches(ServerResponse.class::isInstance)
                .verifyComplete();

        verify(createLoanApplicationUseCase, times(1)).createLoanApplication(domain, "Préstamo Personal");
    }
}