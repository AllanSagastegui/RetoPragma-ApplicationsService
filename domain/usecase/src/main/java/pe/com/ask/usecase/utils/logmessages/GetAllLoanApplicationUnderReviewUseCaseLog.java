package pe.com.ask.usecase.utils.logmessages;

public final class GetAllLoanApplicationUnderReviewUseCaseLog {

    private GetAllLoanApplicationUnderReviewUseCaseLog() {}

    // Main UseCase logs
    public static final String START_USE_CASE =
            "[GetAllLoanApplicationUnderReview - UseCase] Start execution with page={}, size={}, statusFilter={}";
    public static final String STATUS_IDS_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] Retrieved status IDs: {}";
    public static final String LOANS_AND_TOTAL_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] Retrieved total loans={} and loan list size={}";
    public static final String USER_IDS_EXTRACTED =
            "[GetAllLoanApplicationUnderReview - UseCase] Extracted user IDs from loans: {}";
    public static final String STATUS_MAP_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] Retrieved status map with {} entries";
    public static final String CLIENTS_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] Retrieved {} clients";
    public static final String LOAN_TYPES_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] Retrieved {} loan types";
    public static final String LOANS_MAPPED =
            "[GetAllLoanApplicationUnderReview - UseCase] Mapped {} LoanWithClient objects";
    public static final String ERROR_OCCURRED =
            "[GetAllLoanApplicationUnderReview - UseCase] Error executing use case: {}";

    // Get All Loan Types Use Case Logs
    public static final String GET_ALL_LOAN_TYPES_EXECUTING =
            "[GetAllLoanApplicationUnderReview - UseCase] Executing GetAllLoanTypesUseCase";
    public static final String GET_ALL_LOAN_TYPES_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] LoanTypes retrieved: {}";
    public static final String GET_ALL_LOAN_TYPES_ERROR =
            "[GetAllLoanApplicationUnderReview - UseCase] Error retrieving LoanTypes: {}";

    // Get All Status Process Use Case Logs
    public static final String GET_ALL_STATUS_PROCESS_EXECUTING =
            "[GetAllLoanApplicationUnderReview - UseCase] Executing GetAllStatusProcessUseCase";
    public static final String GET_ALL_STATUS_PROCESS_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] Status map retrieved: {}";
    public static final String GET_ALL_STATUS_PROCESS_ERROR =
            "[GetAllLoanApplicationUnderReview - UseCase] Error retrieving status map: {}";


    // Get Clients By Ids Use Case Logs
    public static final String GET_CLIENTS_BY_IDS_EXECUTING =
            "[GetAllLoanApplicationUnderReview - UseCase] Executing GetClientsByIdsUseCase with userIds: {}";
    public static final String GET_CLIENTS_BY_IDS_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] Clients retrieved: {}";
    public static final String GET_CLIENTS_BY_IDS_ERROR =
            "[GetAllLoanApplicationUnderReview - UseCase] Error retrieving clients: {}";

    // Get Loans And Total Use Case Logs
    public static final String GET_LOANS_AND_TOTAL_EXECUTING =
            "[GetAllLoanApplicationUnderReview - UseCase] Executing GetLoansAndTotalUseCase with statusIds={}, page={}, size={}";
    public static final String GET_LOANS_AND_TOTAL_EMPTY_STATUS_IDS =
            "[GetAllLoanApplicationUnderReview - UseCase] statusIds is empty or null, returning empty results";
    public static final String GET_LOANS_AND_TOTAL_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] Total loans={} and loan list size={}";
    public static final String GET_LOANS_AND_TOTAL_ERROR =
            "[GetAllLoanApplicationUnderReview - UseCase] Error retrieving loans and total: {}";

    // Get Status Ids Use Case Logs
    public static final String GET_STATUS_IDS_EXECUTING =
            "[GetAllLoanApplicationUnderReview - UseCase] Executing GetStatusIdsUseCase with filter: {}";
    public static final String GET_STATUS_IDS_RETRIEVED =
            "[GetAllLoanApplicationUnderReview - UseCase] Status IDs retrieved: {}";
    public static final String GET_STATUS_IDS_ERROR =
            "[GetAllLoanApplicationUnderReview - UseCase] Error retrieving Status IDs: {}";

    // Loan Process Use Case Logs
    public static final String LOAN_PROCESS_EXECUTING =
            "[GetAllLoanApplicationUnderReview - UseCase] Executing LoanProcessUseCase for {} loans";
    public static final String LOAN_PROCESS_COMPLETED =
            "[GetAllLoanApplicationUnderReview - UseCase] Completed LoanProcessUseCase for {} loans";
}