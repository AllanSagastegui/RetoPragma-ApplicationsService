package pe.com.ask.sqs.utils.logsmessages;

public final class SQSUpdateLoanApplicationListenerLog {

    private SQSUpdateLoanApplicationListenerLog() {}

    public static final String START_LISTENING = "[SQS - UpdateLoanApplicationListener] Started listening on queue {}";
    public static final String RECEIVED_MESSAGE = "[SQS - UpdateLoanApplicationListener] Processing message {}";
    public static final String PARSE_SUCCESS = "[SQS - UpdateLoanApplicationListener] Message successfully parsed to LoanUpdateResponse";
    public static final String UPDATE_SUCCESS = "[SQS - UpdateLoanApplicationListener] Loan {} updated to {}";
    public static final String UPDATE_ERROR = "[SQS - UpdateLoanApplicationListener] Error updating loan {}: {}";
}