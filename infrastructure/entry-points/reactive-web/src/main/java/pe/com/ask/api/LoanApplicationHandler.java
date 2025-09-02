package pe.com.ask.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseGetLoanApplicationUnderReview;
import pe.com.ask.api.exception.model.UnexpectedException;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.LoanApplicationMapper;
import pe.com.ask.api.utils.logmessages.LoanApplicationLog;
import pe.com.ask.api.utils.routes.Routes;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanwithclient.Pageable;
import pe.com.ask.usecase.createloanapplication.CreateLoanApplicationUseCase;
import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.usecase.getallloanapplicationunderreview.GetAllLoanApplicationUnderReviewUseCase;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class LoanApplicationHandler {

    private final LoanApplicationMapper mapper;
    private final ValidationService validator;
    private final CustomLogger logger;

    private final CreateLoanApplicationUseCase  createLoanApplicationUseCase;
    private final GetAllLoanApplicationUnderReviewUseCase  getAllLoanApplicationUnderReviewUseCase;

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

    public Mono<ServerResponse> listenGETAllLoanApplicationUnderReviewUseCase(ServerRequest serverRequest) {
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));

        return getAllLoanApplicationUnderReviewUseCase.execute(page, size)
                .map(pageResponse -> Pageable.<ResponseGetLoanApplicationUnderReview>builder()
                        .page(pageResponse.getPage())
                        .size(pageResponse.getSize())
                        .totalElements(pageResponse.getTotalElements())
                        .totalPages(pageResponse.getTotalPages())
                        .content(
                                pageResponse.getContent().stream()
                                        .map(mapper::toResponseGetLoanApplicationUnderReview)
                                        .toList()
                        )
                        .build()
                ).flatMap(response ->
                        ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response)
                )
                .onErrorResume(ex -> {
                    logger.trace(LoanApplicationLog.ERROR_OCCURRED, ex.getMessage());
                    return Mono.error(
                            ex instanceof BaseException ? ex : new UnexpectedException(ex)
                    );
                });
    }
}