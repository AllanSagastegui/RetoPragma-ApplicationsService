package pe.com.ask.api.exception.model;

import lombok.Getter;
import pe.com.ask.usecase.exception.BaseException;
import pe.com.ask.usecase.utils.errors.ErrorCatalog;

import java.util.Map;

@Getter
public class ValidationException extends BaseException {

    public ValidationException(Map<String, String> errors) {
        super(
                ErrorCatalog.VALIDATION_EXCEPTION.getErrorCode(),
                ErrorCatalog.VALIDATION_EXCEPTION.getTitle(),
                ErrorCatalog.VALIDATION_EXCEPTION.getMessage(),
                ErrorCatalog.VALIDATION_EXCEPTION.getStatus(),
                errors
        );
    }
}