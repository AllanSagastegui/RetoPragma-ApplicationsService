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
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseCreateLoanApplication;
import reactor.core.publisher.Mono;

@Component
@Schema(
        name = "CreateLoanApplicationDoc",
        description = "API documentation for creating a new loan application. " +
                "This endpoint registers a loan application using applicant details and the selected loan type. " +
                "It validates the input data, ensures the loan type exists, and enforces business rules such as " +
                "validating that the loan amount is within the allowed range. " +
                "Authentication via Bearer token is required."
)
public class CreateLoanApplicationDoc {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Create a new Loan Application",
            description = "Registers a new loan application based on the provided applicant information and selected loan type. " +
                    "Performs validation on the input data and ensures the selected loan type is valid. " +
                    "Requires Bearer token authentication.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Loan Application successfully created",
                    content = @Content(
                            schema = @Schema(implementation =  ResponseCreateLoanApplication.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error or malformed request",
                    content = @Content(
                            schema = @Schema(implementation =  ValidationExceptionDoc.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized: missing or invalid JWT token",
                    content = @Content(
                            schema = @Schema(implementation = UnauthorizedExceptionDoc.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Loan Type Not Found",
                    content = @Content(
                            schema = @Schema(implementation =  LoanTypeNotFoundExceptionDoc.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Loan Amount Out of Range",
                    content = @Content(
                            schema = @Schema(implementation =  LoanAmountOutOfRangeExceptionDoc.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation =  UnexpectedExceptionDoc.class)
                    )
            )
    })
    public Mono<ResponseCreateLoanApplication> createLoanApplicationDoc(
            @RequestBody(description = "Data required to create a new loan application")
            @org.springframework.web.bind.annotation.RequestBody CreateLoanApplicationDTO dto) {
        return Mono.empty();
    }
}