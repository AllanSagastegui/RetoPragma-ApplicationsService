package pe.com.ask.api.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.exception.model.ErrorResponse;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.usecase.exception.BaseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GlobalExceptionFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final CustomLogger logger;

    @Override
    @NonNull
    public Mono<ServerResponse> filter(@NonNull ServerRequest request,
                                       @NonNull HandlerFunction<ServerResponse> next) {
        return next.handle(request)
                .onErrorResume(BaseException.class, ex -> {
                    logger.trace(
                            "Exception: {} - {} - errors: {}",
                            ex.getErrorCode(),
                            ex.getMessage(),
                            ex.getErrors());
                    return ServerResponse.status(ex.getStatus()).bodyValue(
                            ErrorResponse.builder()
                                    .errorCode(ex.getErrorCode())
                                    .tittle(ex.getTitle())
                                    .message(ex.getMessage())
                                    .errors(ex.getErrors())
                                    .status(ex.getStatus())
                                    .timestamp(ex.getTimestamp())
                                    .build()
                    );
                });
    }
}