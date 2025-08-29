package pe.com.ask.api.doc.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "UnexpectedException",
        description = "Error response returned for unexpected or unhandled server errors."
)
public class UnexpectedExceptionDoc {
    @Schema(
            description = "Unique error code for this type of exception.",
            example = "AUTH_INTERNAL_SERVER_ERROR"
    )
    private String errorCode;

    @Schema(
            description = "Short descriptive title of the error.",
            example = "Unexpected Error"
    )
    private String title;

    @Schema(
            description = "Detailed error message explaining the reason for the failure.",
            example = "An unexpected error occurred while processing the request."
    )
    private String message;

    @Schema(
            description = "HTTP status code associated with this error.",
            example = "500"
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