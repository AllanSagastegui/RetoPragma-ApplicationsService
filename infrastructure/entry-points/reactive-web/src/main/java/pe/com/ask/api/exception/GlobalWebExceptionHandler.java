package pe.com.ask.api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import pe.com.ask.api.exception.model.ErrorResponse;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.baseexception.BaseException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Order(-2)
@Configuration
@RequiredArgsConstructor
public class GlobalWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;
    private final CustomLogger logger;

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        if (ex instanceof BaseException baseException) {
            logger.trace(
                    "Exception: {} - {} - errors: {}",
                    baseException.getErrorCode(),
                    baseException.getMessage(),
                    baseException.getErrors());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorCode(baseException.getErrorCode())
                    .tittle(baseException.getTitle())
                    .message(baseException.getMessage())
                    .errors(baseException.getErrors())
                    .status(baseException.getStatus())
                    .timestamp(baseException.getTimestamp() != null ? baseException.getTimestamp() : LocalDateTime.now())
                    .build();

            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(baseException.getStatus()));
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return exchange.getResponse().writeWith(Mono.fromCallable(() -> {
                try {
                    return exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error al serializar la respuesta de error", e);
                }
            }));
        }
        return Mono.error(ex);
    }
}