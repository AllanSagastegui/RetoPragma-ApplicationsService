package pe.com.ask.api.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.UpdateLoanApplicationDTO;
import pe.com.ask.api.exception.model.UnexpectedException;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.LoanApplicationMapper;
import pe.com.ask.api.utils.logmessages.UpdateLoanApplicationLog;
import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.usecase.updateloanapplication.UpdateLoanApplicationUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateLoanApplicationHandler {
    private final LoanApplicationMapper mapper;
    private final ValidationService validator;
    private final CustomLogger logger;

    private final UpdateLoanApplicationUseCase updateLoanApplicationUseCase;

    public Mono<ServerResponse> listenPUTUpdateLoanApplicationUseCase(ServerRequest serverRequest) {
        UUID idLoanApplication = UUID.fromString(serverRequest.pathVariable("idLoanApplication"));

        return serverRequest.bodyToMono(UpdateLoanApplicationDTO.class)
                .doOnSubscribe(sub -> logger.trace(UpdateLoanApplicationLog.START_UPDATE_FLOW, idLoanApplication))
                .doOnNext(dto -> logger.trace(UpdateLoanApplicationLog.RECEIVED_BODY, dto))
                .flatMap(validator::validate)
                .doOnNext(dto -> logger.trace(UpdateLoanApplicationLog.VALIDATION_SUCCESS, dto.status()))
                .flatMap(dto -> updateLoanApplicationUseCase.execute(idLoanApplication, dto.status()))
                .doOnNext(app -> logger.trace(UpdateLoanApplicationLog.UPDATE_SUCCESS, app))
                .map(mapper::toResponseUpdateLoanApplication)
                .flatMap(response ->
                        ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response))
                .doOnSuccess(resp -> logger.trace(UpdateLoanApplicationLog.RESPONSE_OK, idLoanApplication))
                .onErrorResume(ex -> {
                    logger.trace(UpdateLoanApplicationLog.ERROR_OCCURRED, ex.getMessage());
                    return Mono.error(
                            ex instanceof BaseException ? ex : new UnexpectedException(ex)
                    );
                });
    }
}