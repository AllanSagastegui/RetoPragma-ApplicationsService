package pe.com.ask.api.utils.logmessages;

public final class LoanApplicationLog {

    private LoanApplicationLog() {}

    public static final String START_FLOW = "[LoanApplication - Handler] Start create loan application request processing";
    public static final String RECEIVED_BODY = "[LoanApplication - Handler] Received request body";
    public static final String VALIDATION_SUCCESS = "[LoanApplication - Handler] Validation successful for loan type: {}";
    public static final String MAPPED_TO_DOMAIN = "[LoanApplication - Handler] Mapped DTO to domain";
    public static final String LOAN_CREATION_SUCCESS = "[LoanApplication - Handler] Loan application created successfully";
    public static final String RESPONSE_CREATED = "[LoanApplication - Handler] ServerResponse successfully created with ID: {}";
    public static final String ERROR_OCCURRED = "[LoanApplication - Handler] Error during loan application creation: {}";
}