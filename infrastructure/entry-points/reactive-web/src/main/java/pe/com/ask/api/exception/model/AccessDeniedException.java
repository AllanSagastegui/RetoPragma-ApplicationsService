package pe.com.ask.api.exception.model;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class AccessDeniedException extends BaseException {
    public AccessDeniedException() {
        super(
                ErrorCatalog.ACCESS_DENIED.getErrorCode(),
                ErrorCatalog.ACCESS_DENIED.getTitle(),
                ErrorCatalog.ACCESS_DENIED.getMessage(),
                ErrorCatalog.ACCESS_DENIED.getStatus(),
                ErrorCatalog.ACCESS_DENIED.getErrors()
        );
    }
}