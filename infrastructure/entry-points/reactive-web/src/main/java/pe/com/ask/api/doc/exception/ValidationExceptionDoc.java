package pe.com.ask.api.doc.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "ValidationException",
        description = "Represents a validation error with details about invalid fields"
)
public class ValidationExceptionDoc {
    @Schema(
            description = "Unique code that identifies the type of error",
            example = "AUTH_VALIDATION_EXCEPTION"
    )
    private String errorCode;

    @Schema(
            description = "General title of the error",
            example = "Field validation error"
    )
    private String title;

    @Schema(
            description = "Detailed explanatory message about the error",
            example = "Some fields do not meet the validation requirements"
    )
    private String message;

    @Schema(
            description = "HTTP status code associated with the error",
            example = "400"
    )
    private int status;

    @Schema(
            description = "Date and time when the error occurred",
            example = "2025-08-28T12:45:30"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Map of specific field errors (key: field name, value: error message)",
            example = "{\"dni\": \"DNI cannot be blank\", \"loanType\": \"Loan Type cannot be blank\"}"
    )
    private Object errors;
}