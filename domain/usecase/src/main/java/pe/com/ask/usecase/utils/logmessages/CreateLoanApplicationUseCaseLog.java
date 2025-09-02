package pe.com.ask.usecase.utils.logmessages;

public final class CreateLoanApplicationUseCaseLog {

    private CreateLoanApplicationUseCaseLog() {}

    // Main UseCase
    public static final String START_FLOW =
            "[CreateLoanApplication - UseCase] Start create loan application flow for DNI: {}";
    public static final String DNI_MISMATCH =
            "[CreateLoanApplication - UseCase] DNI mismatch. Loan DNI: {}, Token DNI: {}";
    public static final String ERROR_OCCURRED =
            "[CreateLoanApplication - UseCase] Error creating loan application for DNI {}: {}";

    // Validate Loan Type Use Case
    public static final String LOAN_TYPE_FOUND =
            "[CreateLoanApplication - UseCase] Loan type found: {}";
    public static final String LOAN_TYPE_NOT_FOUND =
            "[CreateLoanApplication - UseCase] Loan type not found: {}";

    // Validate Loan Amount Use Case
    public static final String LOAN_AMOUNT_OUT_OF_RANGE =
            "[CreateLoanApplication - UseCase] Loan amount {} is out of range for type: {}";

    // Get Default Status Use Case
    public static final String STATUS_NOT_FOUND =
            "[CreateLoanApplication - UseCase] Default status not found: {}";

    // Persist Loan Application Use Case
    public static final String LOAN_APPLICATION_CREATED =
            "[CreateLoanApplication - UseCase] Loan application created with ID: {}";
}
