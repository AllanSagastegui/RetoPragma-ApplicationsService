package pe.com.ask.api;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseCreateLoanApplication;
import pe.com.ask.api.exception.model.UnexpectedException;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.LoanApplicationMapper;
import pe.com.ask.api.utils.logmessages.LoanApplicationLog;
import pe.com.ask.api.utils.routes.Routes;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.usecase.createloanapplication.CreateLoanApplicationUseCase;
import pe.com.ask.usecase.exception.BaseException;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class LoanApplicationHandler {

    private final LoanApplicationMapper mapper;
    private final ValidationService validator;
    private final CustomLogger logger;

    private final CreateLoanApplicationUseCase  createLoanApplicationUseCase;

    public Mono<ServerResponse> listenPOSTCreateLoanApplicationUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateLoanApplicationDTO.class)
                .doOnSubscribe(sub -> logger.trace(LoanApplicationLog.START_FLOW))
                .doOnNext(dto -> logger.trace(LoanApplicationLog.RECEIVED_BODY, dto))
                .flatMap(validator::validate)
                .doOnNext(dto -> logger.trace(LoanApplicationLog.VALIDATION_SUCCESS, dto.loanType()))
                .flatMap(dto -> {
                    var domain = mapper.toDomain(dto);
                    logger.trace(LoanApplicationLog.MAPPED_TO_DOMAIN, domain);
                    return createLoanApplicationUseCase.createLoanApplication(domain, dto.loanType());
                })
                .doOnNext(app -> logger.trace(LoanApplicationLog.LOAN_CREATION_SUCCESS, app))
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.created(URI.create(Routes.CREATE_LOAN_APPLICATION))
                        .bodyValue(response))
                .doOnSuccess(resp -> logger.trace(LoanApplicationLog.RESPONSE_CREATED))
                .onErrorResume(ex -> {
                    logger.trace(LoanApplicationLog.ERROR_OCCURRED, ex.getMessage());
                    return Mono.error(
                            ex instanceof BaseException ? ex : new UnexpectedException(ex)
                    );
                });
    }

    public Mono<ResponseCreateLoanApplication> createLoanApplicationDoc(
            @RequestBody(description = "Loan Application - Data required to create a new loan application")
            CreateLoanApplicationDTO dto) {
        return Mono.empty();
    }
}
