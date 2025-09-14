package pe.com.ask.sqs.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.gateways.UpdateReportsSQSGateway;
import pe.com.ask.sqs.config.SQSSender;
import pe.com.ask.sqs.config.SQSSenderProperties;
import pe.com.ask.sqs.utils.parser.JsonParser;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SQSUpdateReports implements UpdateReportsSQSGateway {

    private final SQSSenderProperties properties;
    private final SQSSender sqsSender;
    private final JsonParser jsonParser;
    private final CustomLogger logger;

    @Override
    public Mono<Void> updateReports(String status, BigDecimal amount) {
        return Mono.fromCallable(() -> {
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("status", status);
                    payload.put("amount", amount);

                    logger.trace("[SQSUpdateReports] Serializando payload con status {}", status);
                    return jsonParser.toJson(payload);
                })
                .flatMap(json -> {
                    logger.trace("[SQSUpdateReports] Enviando mensaje a {}", properties.queueUpdateReports());
                    return sqsSender.send(json, properties.queueUpdateReports());
                })
                .doOnSuccess(v -> logger.trace("[SQSUpdateReports] Mensaje enviado correctamente con status {}", status))
                .doOnError(err -> logger.error("[SQSUpdateReports] Error al enviar mensaje con status {}: {}", status, err.getMessage(), err))
                .then();
    }
}