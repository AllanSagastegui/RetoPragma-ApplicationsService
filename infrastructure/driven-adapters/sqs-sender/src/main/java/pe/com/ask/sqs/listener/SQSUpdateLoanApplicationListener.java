package pe.com.ask.sqs.listener;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.sqs.config.SQSListener;
import pe.com.ask.sqs.dto.LoanUpdateResponse;
import pe.com.ask.sqs.utils.parser.JsonParser;
import pe.com.ask.sqs.utils.logsmessages.SQSUpdateLoanApplicationListenerLog;
import pe.com.ask.usecase.updateloanapplication.UpdateLoanApplicationUseCase;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SQSUpdateLoanApplicationListener {

    private final SQSListener sqsListener;
    private final UpdateLoanApplicationUseCase updateLoanApplicationUseCase;
    private final JsonParser jsonParser;
    private final CustomLogger logger;

    @Value("${adapter.sqs.queueResponseCalculateCapacity}")
    private String responseQueueUrl;

    @PostConstruct
    public void init() {
        logger.trace(SQSUpdateLoanApplicationListenerLog.START_LISTENING, responseQueueUrl);
        sqsListener.startListening(responseQueueUrl, this::processMessage);
    }

    private Mono<Void> processMessage(Message message) {
        return Mono.fromCallable(() -> {
                    logger.trace(SQSUpdateLoanApplicationListenerLog.RECEIVED_MESSAGE, message);
                    return jsonParser.fromJson(message.body(), LoanUpdateResponse.class);
                })
                .doOnNext(dto -> logger.trace(SQSUpdateLoanApplicationListenerLog.PARSE_SUCCESS))
                .flatMap(dto -> {
                    UUID loanApplicationId = UUID.fromString(dto.response().loanApplicationId());
                    String status = dto.response().status();

                    return updateLoanApplicationUseCase.execute(loanApplicationId, status)
                            .doOnSuccess(v -> logger.trace(SQSUpdateLoanApplicationListenerLog.UPDATE_SUCCESS, loanApplicationId, status))
                            .doOnError(err -> logger.trace(SQSUpdateLoanApplicationListenerLog.UPDATE_ERROR,
                                    loanApplicationId, err.getMessage()))
                            .onErrorResume(err -> Mono.empty());
                })
                .then();
    }
}