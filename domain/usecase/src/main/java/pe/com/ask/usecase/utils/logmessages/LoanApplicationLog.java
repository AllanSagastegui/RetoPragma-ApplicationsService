package pe.com.ask.usecase.utils.logmessages;

public final class LoanApplicationLog {

    private LoanApplicationLog() {}

    public static final String START_FLOW = "[LoanApplication - UseCase] Start create loan application flow for DNI: {}";
    public static final String LOAN_TYPE_FOUND = "[LoanApplication - UseCase] Loan type found: {}";
    public static final String LOAN_TYPE_NOT_FOUND = "[LoanApplication - UseCase] Loan type not found: {}";
    public static final String LOAN_AMOUNT_OUT_OF_RANGE = "[LoanApplication - UseCase] Loan amount {} is out of range for type: {}";
    public static final String STATUS_NOT_FOUND = "[LoanApplication - UseCase] Status not found: {}";
    public static final String LOAN_APPLICATION_CREATED = "[LoanApplication - UseCase] Loan application created with ID: {}";
    public static final String ERROR_OCCURRED = "[LoanApplication - UseCase] Error creating loan application for DNI {}: {}";
}