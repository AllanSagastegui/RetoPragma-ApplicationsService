package pe.com.ask.api.exception.service;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.com.ask.api.exception.model.ValidationException;
import reactor.test.StepVerifier;

class ValidationServiceTest {
    private ValidationService validationService;

    @BeforeEach
    void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            validationService = new ValidationService(validator);
        }
    }

    static class TestDTO {
        @NotNull
        String name;
        TestDTO(String name) { this.name = name; }
    }

    @Test
    @DisplayName("Should pass validation when object is valid")
    void validateValidObject() {
        TestDTO dto = new TestDTO("Allan");

        StepVerifier.create(validationService.validate(dto))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ValidationException when object is invalid")
    void validateInvalidObject() {
        TestDTO dto = new TestDTO(null);

        StepVerifier.create(validationService.validate(dto))
                .expectError(ValidationException.class)
                .verify();
    }
}