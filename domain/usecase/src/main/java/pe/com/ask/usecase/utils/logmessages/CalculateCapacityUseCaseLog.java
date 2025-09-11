package pe.com.ask.usecase.utils.logmessages;

public final class CalculateCapacityUseCaseLog {

    private CalculateCapacityUseCaseLog() {}

    public static final String START_FLOW =
            "[CalculateCapacity - UseCase] Starting calculate capacity for loanApplicationId={}";
    public static final String LOAN_TYPE_FOUND =
            "[CalculateCapacity - UseCase] LoanType {} found with ID: {}";
    public static final String LOAN_TYPE_NO_AUTOMATIC =
            "[CalculateCapacity - UseCase] LoanType {} does not require automatic validation. Terminating flow.";
    public static final String FETCHING_CLIENT =
            "[CalculateCapacity - UseCase] Fetching client snapshot for userId={}";
    public static final String CLIENT_FOUND =
            "[CalculateCapacity - UseCase] Client snapshot found for userId={}, present={}";
    public static final String PUBLISHING_DECISION =
            "[CalculateCapacity - UseCase] Publishing decision. loanApplicationId={}, approvedLoansCount={}, clientPresent={}";
    public static final String SUCCESS =
            "[CalculateCapacity - UseCase] Calculate capacity flow finished for loanApplicationId={}";
    public static final String ERROR_OCCURRED =
            "[CalculateCapacity - UseCase] Error calculating capacity for loanApplicationId={}, error={}";
}