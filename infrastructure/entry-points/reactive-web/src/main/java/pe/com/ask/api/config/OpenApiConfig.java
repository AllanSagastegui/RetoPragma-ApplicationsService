package pe.com.ask.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Loan Application Service API",
                version = "1.5",
                description = "The Loan Application Service is responsible for managing the full lifecycle of loan applications within the Crediya ecosystem. " +
                        "It provides endpoints for creating, updating, retrieving, and managing applications, including applicant validation and loan type processing. " +
                        "This service ensures a secure and efficient loan request flow integrated with other Crediya services.",
                        contact = @Contact(
                                name = "Allan Sagastegui",
                                email = "sagasteguiherradaa@gmail.com",
                                url = "https://github.com/AllanSagastegui/RetoPragma-ApplicationsService"
                        )
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}