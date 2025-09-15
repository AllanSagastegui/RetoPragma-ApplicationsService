package pe.com.ask.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "UpdateLoanApplicationDTO",
        description = "Data required to update the status of an existing loan application"
)
public record UpdateLoanApplicationDTO(

        @NotBlank(message = "Status cannot be blank")
        @Schema(
                description = "New status of the loan application",
                example = "Aprobada"
        )
        String status
) { }