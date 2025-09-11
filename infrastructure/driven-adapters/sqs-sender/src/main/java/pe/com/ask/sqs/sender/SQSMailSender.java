package pe.com.ask.sqs.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.gateways.PublishDecisionSQSGateway;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.sqs.config.SQSSender;
import pe.com.ask.sqs.config.SQSSenderProperties;
import pe.com.ask.sqs.utils.parser.JsonParser;
import pe.com.ask.sqs.utils.logsmessages.SQSMailSenderLog;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SQSMailSender implements PublishDecisionSQSGateway {

    private final SQSSenderProperties properties;
    private final SQSSender sqsSender;
    private final JsonParser jsonParser;
    private final CustomLogger logger;

    @Override
    public Mono<Void> publishDecision(LoanWithClient loanWithClient) {
        return Mono.fromCallable(() -> {
                    logger.trace(SQSMailSenderLog.SERIALIZING_MESSAGE,
                            loanWithClient.getLoanApplicationData().getIdLoanApplication());
                    return jsonParser.toJson(loanWithClient);
                })
                .doOnNext(json -> logger.trace(SQSMailSenderLog.MESSAGE_READY))
                .flatMap(json -> {
                    logger.trace(SQSMailSenderLog.SENDING_MESSAGE, properties.queueUrl());
                    return sqsSender.send(json, properties.queueUrl());
                })
                .doOnSuccess(v -> logger.trace(SQSMailSenderLog.MESSAGE_SENT_SUCCESS,
                        loanWithClient.getLoanApplicationData().getIdLoanApplication()))
                .doOnError(err -> logger.trace(SQSMailSenderLog.MESSAGE_SENT_ERROR,
                        loanWithClient.getLoanApplicationData().getIdLoanApplication(), err))
                .then();
    }
}