package pe.com.ask.sqs.utils.logsmessages;

public final class SQSUpdateReportsLog {

    private SQSUpdateReportsLog() {}

    public static final String SERIALIZING_PAYLOAD = "[SQS - UpdateReports] Serializing payload with status {}";
    public static final String PAYLOAD_READY = "[SQS - UpdateReports] Payload serialized successfully";
    public static final String SENDING_MESSAGE = "[SQS - UpdateReports] Sending message to SQS queue {}";
    public static final String MESSAGE_SENT_SUCCESS = "[SQS - UpdateReports] Message sent successfully with status {}";
    public static final String MESSAGE_SENT_ERROR = "[SQS - UpdateReports] Error sending message with status {} - Cause: {}";
}