package pe.com.ask.model.baseexception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum ErrorCatalog {
    STATUS_NOT_FOUND(
            "APPLICATIONS_STATUS_NOT_FOUND",
            "Status Not Found",
            "The requested status could not be found. Please make sure it exists in the system.",
            404,
            Map.of(
                    "status", "The status you provided does not exist. Please verify and try again."
            )
    ),
    LOAN_TYPE_NOT_FOUND(
            "APPLICATIONS_LOAN_TYPE_NOT_FOUND",
            "Loan Type Not Found",
            "The selected loan type does not exist. Please choose a valid type.",
            404,
            Map.of(
                    "loanType", "The loan type you selected is invalid. Please select a valid loan type."
            )
    ),
    LOAN_APPLICATION_NOT_FOUND(
            "APPLICATIONS_LOAN_APPLICATION_NOT_FOUND",
            "Loan Application Not Found",
            "The requested loan application could not be found. Please check the ID and try again.",
            404,
            Map.of(
                    "loanApplication", "The loan application ID you provided does not exist in the system."
            )
    ),
    CLIENT_NOT_FOUND(
            "APPLICATIONS_CLIENT_NOT_FOUND",
            "Client Not Found",
            "The client associated with this request could not be found.",
            404,
            Map.of(
                    "client", "The client ID you provided does not exist in the system."
            )
    ),
    VALIDATION_EXCEPTION(
            "APPLICATIONS_VALIDATION_EXCEPTION",
            "Validation Failed",
            "Oops! Some of the data you sent doesn’t look right. Please review the fields and try again.",
            400,
            null
    ),
    INTERNAL_SERVER_ERROR(
            "APPLICATIONS_INTERNAL_SERVER_ERROR",
            "Internal Server Error",
            "Something went wrong on our side. Please try again later or contact support if the issue persists.",
            500,
            Map.of("server", "Unexpected error occurred")
    ),
    LOAN_AMOUNT_OUT_OF_RANGE(
            "APPLICATIONS_LOAN_AMOUNT_OUT_OF_RANGE",
            "Loan Amount Out of Range",
            "The requested loan amount is not within the allowed range for the selected loan type.",
            422,
            Map.of(
                    "amount", "The loan amount must be between the minimum and maximum allowed for this loan type."
            )
    ),
    UNAUTHORIZED(
            "AUTH_UNAUTHORIZED",
            "Unauthorized",
            "You are not authenticated. Please log in to access this resource.",
            401,
            Map.of("user", "Authentication required")
    ),
    ACCESS_DENIED(
            "AUTH_ACCESS_DENIED",
            "Access Denied",
            "You do not have permission to access this resource.",
            403,
            Map.of("user", "You lack the necessary permissions")
    ),
    DNI_MISMATCH(
            "APPLICATIONS_DNI_MISMATCH",
            "DNI Mismatch",
            "The DNI in the request does not match the authenticated user's DNI.",
            403,
            Map.of(
                    "dni", "The DNI provided does not belong to the authenticated user"
            )
    );


    private final String errorCode;
    private final String title;
    private final String message;
    private final int status;
    private final Map<String, String> errors;
}
