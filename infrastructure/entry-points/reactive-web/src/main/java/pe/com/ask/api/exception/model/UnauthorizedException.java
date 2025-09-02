package pe.com.ask.api.exception.model;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super(
                ErrorCatalog.UNAUTHORIZED.getErrorCode(),
                ErrorCatalog.UNAUTHORIZED.getTitle(),
                ErrorCatalog.UNAUTHORIZED.getMessage(),
                ErrorCatalog.UNAUTHORIZED.getStatus(),
                ErrorCatalog.UNAUTHORIZED.getErrors()
        );
    }
}