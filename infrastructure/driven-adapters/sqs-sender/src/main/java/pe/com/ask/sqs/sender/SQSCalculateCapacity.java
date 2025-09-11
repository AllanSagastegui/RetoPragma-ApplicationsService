package pe.com.ask.sqs.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.CalculateCapacitySQSGateway;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.sqs.config.SQSSender;
import pe.com.ask.sqs.config.SQSSenderProperties;
import pe.com.ask.sqs.utils.parser.JsonParser;
import pe.com.ask.sqs.utils.logsmessages.SQSCalculateCapacityLog;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SQSCalculateCapacity implements CalculateCapacitySQSGateway {

    private final SQSSenderProperties properties;
    private final SQSSender sqsSender;
    private final JsonParser jsonParser;
    private final CustomLogger logger;

    @Override
    public Mono<Void> publishDecision(
            ClientSnapshot clientSnapshot,
            List<LoanApplication> loanApplicationData,
            LoanApplication loanApplication
    ) {
        return Mono.fromCallable(() -> {
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("client", clientSnapshot);
                    payload.put("approvedLoanApplications", loanApplicationData);
                    payload.put("newLoanApplication", loanApplication);

                    logger.trace(SQSCalculateCapacityLog.SERIALIZING_PAYLOAD, loanApplication.getIdLoanApplication());
                    return jsonParser.toJson(payload);
                })
                .flatMap(json -> {
                    logger.trace(SQSCalculateCapacityLog.SENDING_MESSAGE, properties.queueCalculateCapacity());
                    return sqsSender.send(json, properties.queueCalculateCapacity());
                })
                .doOnSuccess(v -> logger.trace(SQSCalculateCapacityLog.MESSAGE_SENT, loanApplication.getIdLoanApplication()))
                .doOnError(err -> logger.trace(SQSCalculateCapacityLog.MESSAGE_ERROR,
                        loanApplication.getIdLoanApplication(), err.getMessage(), err))
                .then();
    }
}