package pe.com.ask.api.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.response.ResponseGetLoanApplicationUnderReview;
import pe.com.ask.api.exception.model.UnexpectedException;
import pe.com.ask.api.mapper.LoanApplicationMapper;
import pe.com.ask.api.utils.logmessages.LoanApplicationLog;
import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanwithclient.Pageable;
import pe.com.ask.usecase.getallloanapplicationunderreview.GetAllLoanApplicationUnderReviewUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GetAllLoanApplicationUnderReviewHandler {

    private final LoanApplicationMapper mapper;
    private final CustomLogger logger;

    private final GetAllLoanApplicationUnderReviewUseCase getAllLoanApplicationUnderReviewUseCase;

    public Mono<ServerResponse> listenGETAllLoanApplicationUnderReviewUseCase(ServerRequest serverRequest) {
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        String statusName = serverRequest.queryParam("statusName").orElse("");

        return getAllLoanApplicationUnderReviewUseCase.execute(page, size, statusName)
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
