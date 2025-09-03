package pe.com.ask.api.doc.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Schema(
        name = "UnauthorizedException",
        description = "Represents an unauthorized error when the user is not authenticated"
)
public class UnauthorizedExceptionDoc {

    @Schema(
            description = "Unique code that identifies the type of error",
            example = "AUTH_UNAUTHORIZED"
    )
    private String errorCode;

    @Schema(
            description = "General title of the error",
            example = "Unauthorized"
    )
    private String title;

    @Schema(
            description = "Detailed explanatory message about the error",
            example = "You are not authenticated. Please log in to access this resource."
    )
    private String message;

    @Schema(
            description = "HTTP status code associated with the error",
            example = "401"
    )
    private int status;

    @Schema(
            description = "Date and time when the error occurred",
            example = "2025-09-02T14:30:00"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Map with additional error details",
            example = "{\"user\": \"Authentication required\"}"
    )
    private Map<String, String> errors;
}