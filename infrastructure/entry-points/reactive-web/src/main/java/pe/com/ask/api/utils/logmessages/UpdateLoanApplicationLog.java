package pe.com.ask.api.utils.logmessages;

public final class UpdateLoanApplicationLog {

    private UpdateLoanApplicationLog() {}

    public static final String START_UPDATE_FLOW = "[UpdateLoanApplication - Handler] Start update loan application request with ID: {}";
    public static final String RECEIVED_BODY = "[UpdateLoanApplication - Handler] Received request body: {}";
    public static final String VALIDATION_SUCCESS = "[UpdateLoanApplication - Handler] Validation successful for status: {}";
    public static final String UPDATE_SUCCESS = "[UpdateLoanApplication - Handler] Loan application updated successfully: {}";
    public static final String RESPONSE_OK = "[UpdateLoanApplication - Handler] ServerResponse OK for loan application ID: {}";
    public static final String ERROR_OCCURRED = "[UpdateLoanApplication - Handler] Error during loan application update: {}";
}