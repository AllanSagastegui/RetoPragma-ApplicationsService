package pe.com.ask.usecase.utils.logmessages;

public final class UpdateLoanApplicationUseCaseLog {

    private UpdateLoanApplicationUseCaseLog() {}

    // Main UseCase
    public static final String START_FLOW =
            "[UpdateLoanApplication - UseCase] Start update loan application with ID: {}, New Status: {}";
    public static final String STATUS_FOUND =
            "[UpdateLoanApplication - UseCase] Status {} found with ID: {}";
    public static final String LOAN_APPLICATION_FOUND =
            "[UpdateLoanApplication - UseCase] Loan application {} found with current status ID: {}";
    public static final String STATUS_UPDATED =
            "[UpdateLoanApplication - UseCase] Loan application {} status updated to {}";
    public static final String CLIENT_FOUND =
            "[UpdateLoanApplication - UseCase] Client {} found with DNI: {}";
    public static final String LOAN_TYPE_FOUND =
            "[UpdateLoanApplication - UseCase] Loan type {} found with name: {}";
    public static final String SUCCESS =
            "[UpdateLoanApplication - UseCase] Loan application {} updated successfully with status: {}";
    public static final String ERROR_OCCURRED =
            "[UpdateLoanApplication - UseCase] Error updating loan application {}: {}";
}