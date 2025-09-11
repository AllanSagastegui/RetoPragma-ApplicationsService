package pe.com.ask.sqs.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class SQSListener {

    private final SqsAsyncClient sqsAsyncClient;

    public void startListening(String queueUrl, Function<Message, Mono<Void>> processor) {
        listen(queueUrl, processor);
    }

    private void listen(String queueUrl, Function<Message, Mono<Void>> processor) {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(20)
                .maxNumberOfMessages(10)
                .build();

        sqsAsyncClient.receiveMessage(request)
                .thenAccept(response -> {
                    List<Message> messages = response.messages();
                    for (Message msg : messages) {
                        processor.apply(msg)
                                .doFinally(sig -> sqsAsyncClient.deleteMessage(builder ->
                                        builder.queueUrl(queueUrl)
                                                .receiptHandle(msg.receiptHandle())))
                                .subscribe();
                    }
                    listen(queueUrl, processor);
                })
                .exceptionally(e -> {
                    log.error("Error receiving messages from {}", queueUrl, e);
                    listen(queueUrl, processor);
                    return null;
                });
    }
}