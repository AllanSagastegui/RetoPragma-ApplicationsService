package pe.com.ask.api.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.com.ask.api.doc.exception.*;
import pe.com.ask.api.dto.request.UpdateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseUpdateLoanApplication;
import reactor.core.publisher.Mono;

@Component
@Schema(
        name = "UpdateLoanApplicationDoc",
        description = "API documentation for updating an existing loan application. " +
                "This operation allows changing the status of a loan application " +
                "identified by its unique ID."
)
public class UpdateLoanApplicationDoc {

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update an existing Loan Application",
            description = "Updates the status or details of an existing loan application identified by its ID.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Loan Application successfully updated",
                    content = @Content(
                            schema = @Schema(implementation = ResponseUpdateLoanApplication.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error or malformed request",
                    content = @Content(
                            schema = @Schema(implementation = ValidationExceptionDoc.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized: missing or invalid JWT token",
                    content = @Content(
                            schema = @Schema(implementation = UnauthorizedExceptionDoc.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Loan Application Not Found",
                    content = @Content(
                            schema = @Schema(implementation = LoanApplicationNotFoundExceptionDoc.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = UnexpectedExceptionDoc.class)
                    )
            )
    })
    public Mono<ResponseUpdateLoanApplication> updateLoanApplicationDoc(
            @RequestBody(description = "Data required to update an existing loan application")
            @org.springframework.web.bind.annotation.RequestBody UpdateLoanApplicationDTO dto
    ) {
        return Mono.empty();
    }
}