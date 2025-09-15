package pe.com.ask.api.doc.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(
        name = "LoanApplicationNotFoundException",
        description = "Error response returned when a loan application could not be found."
)
public class LoanApplicationNotFoundExceptionDoc {
    @Schema(
            description = "Unique error code for this type of exception.",
            example = "APPLICATIONS_LOAN_APPLICATION_NOT_FOUND"
    )
    private String errorCode;

    @Schema(
            description = "Short descriptive title of the error.",
            example = "Loan Application Not Found"
    )
    private String title;

    @Schema(
            description = "Detailed error message explaining the reason for the failure.",
            example = "The requested loan application could not be found. Please check the ID and try again."
    )
    private String message;

    @Schema(
            description = "HTTP status code associated with this error.",
            example = "404"
    )
    private int status;

    @Schema(
            description = "The timestamp when the error occurred.",
            example = "2025-08-28T16:10:30"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Additional information related to the error (if applicable).",
            example = "{ \"loanApplication\": \"The loan application ID you provided does not exist in the system.\" }"
    )
    private Object errors;
}