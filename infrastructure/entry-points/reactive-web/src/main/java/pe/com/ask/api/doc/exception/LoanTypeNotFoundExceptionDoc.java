package pe.com.ask.api.doc.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "LoanTypeNotFoundException",
        description = "Error response returned for unexpected or unhandled server errors."
)
public class LoanTypeNotFoundExceptionDoc {
    @Schema(
            description = "Unique error code for this type of exception.",
            example = "APPLICATIONS_LOAN_TYPE_NOT_FOUND"
    )
    private String errorCode;

    @Schema(
            description = "Short descriptive title of the error.",
            example = "Loan Type Not Found"
    )
    private String title;

    @Schema(
            description = "Detailed error message explaining the reason for the failure.",
            example = "The selected loan type does not exist. Please choose a valid type."
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
            example = "Not Applicable"
    )
    private Object errors;
}