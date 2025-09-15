package pe.com.ask.api.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.com.ask.api.doc.exception.UnauthorizedExceptionDoc;
import pe.com.ask.api.doc.exception.UnexpectedExceptionDoc;
import pe.com.ask.api.dto.response.ResponseGetLoanApplicationUnderReview;
import pe.com.ask.model.loanwithclient.Pageable;
import reactor.core.publisher.Mono;

@Component
@Schema(
        name = "GetAllLoanApplicationUnderReviewDoc",
        description = "API documentation for retrieving all loan applications currently under review. " +
                "This endpoint provides a paginated list of loan applications that are in the review process. " +
                "Clients can customize the request using pagination parameters (`page`, `size`) and an optional " +
                "filter by loan status. Authentication via Bearer token is required."
)
public class GetAllLoanApplicationUnderReviewDoc {

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all loan applications under review",
            description = "Fetches a paginated list of loan applications that are currently under review. " +
                    "Supports pagination and optional filtering by loan status. Requires Bearer token authentication.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of loan applications successfully retrieved",
                    content = @Content(
                            schema = @Schema(
                                    implementation = Pageable.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized: missing or invalid JWT token",
                    content = @Content(
                            schema = @Schema(implementation = UnauthorizedExceptionDoc.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = UnexpectedExceptionDoc.class))
            )
    })
    public Mono<Pageable<ResponseGetLoanApplicationUnderReview>> getAllLoanApplicationsUnderReviewDoc(
            @Parameter(description = "Page number (default: 0)")
            @RequestParam(required = false, defaultValue = "0") Integer page,

            @Parameter(description = "Page size (default: 10)")
            @RequestParam(required = false, defaultValue = "10") Integer size,

            @Parameter(description = "Filter by loan status name (optional)")
            @RequestParam(required = false) String statusName
    ) {
        return Mono.empty();
    }
}