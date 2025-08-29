package pe.com.ask.api.doc.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "LoanAmountOutOfRangeException",
        description = "Represents a validation error with details about invalid fields"
)
public class LoanAmountOutOfRangeExceptionDoc {
    @Schema(
            description = "Unique code that identifies the type of error",
            example = "APPLICATIONS_LOAN_AMOUNT_OUT_OF_RANGE"
    )
    private String errorCode;

    @Schema(
            description = "General title of the error",
            example = "Loan Amount Out of Range"
    )
    private String title;

    @Schema(
            description = "Detailed explanatory message about the error",
            example = "The requested loan amount is not within the allowed range for the selected loan type."
    )
    private String message;

    @Schema(
            description = "HTTP status code associated with the error",
            example = "422"
    )
    private int status;

    @Schema(
            description = "Date and time when the error occurred",
            example = "2025-08-28T12:45:30"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Map of specific field errors (key: field name, value: error message)",
            example = "Not Applicable"
    )
    private Object errors;
}