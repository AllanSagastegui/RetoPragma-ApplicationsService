package pe.com.ask.sqs.utils.logsmessages;

public final class SQSCalculateCapacityLog {

    private SQSCalculateCapacityLog() {}

    public static final String SERIALIZING_PAYLOAD = "[SQS - CalculateCapacity] Serializing payload for loan {}";
    public static final String SENDING_MESSAGE = "[SQS - CalculateCapacity] Sending payload to SQS queue {}";
    public static final String MESSAGE_SENT = "[SQS - CalculateCapacity] Message successfully sent for loan {}";
    public static final String MESSAGE_ERROR = "[SQS - CalculateCapacity] Error sending message for loan {}: {}";
}