package pe.com.ask.api.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.com.ask.api.doc.exception.LoanAmountOutOfRangeExceptionDoc;
import pe.com.ask.api.doc.exception.LoanTypeNotFoundExceptionDoc;
import pe.com.ask.api.doc.exception.UnexpectedExceptionDoc;
import pe.com.ask.api.doc.exception.ValidationExceptionDoc;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseCreateLoanApplication;
import reactor.core.publisher.Mono;

@Component
@Schema
public class CreateLoanApplicationDoc {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Create a new Loan Application",
            description = "Registers a new loan application based on the provided applicant information and selected loan type."
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
            @RequestBody(description = "Loan Application - Data required to create a new loan application")
            @org.springframework.web.bind.annotation.RequestBody CreateLoanApplicationDTO dto) {
        return Mono.empty();
    }
}