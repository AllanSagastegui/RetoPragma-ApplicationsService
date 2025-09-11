package pe.com.ask.sqs.utils.logsmessages;

public final class SQSMailSenderLog {

    private SQSMailSenderLog() {}

    public static final String SERIALIZING_MESSAGE = "[SQS - MailSender] Serializing LoanWithClient for loan {}";
    public static final String MESSAGE_READY = "[SQS - MailSender] LoanWithClient serialized successfully";
    public static final String SENDING_MESSAGE = "[SQS - MailSender] Sending serialized message to SQS queue {}";
    public static final String MESSAGE_SENT_SUCCESS = "[SQS - MailSender] Message successfully sent for loan {}";
    public static final String MESSAGE_SENT_ERROR = "[SQS - MailSender] Error sending message for loan {} - Cause: {}";
}