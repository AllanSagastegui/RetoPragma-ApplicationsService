package pe.com.ask.api.exception.model;

import pe.com.ask.usecase.exception.BaseException;
import pe.com.ask.usecase.utils.ErrorCatalog;

public class UnexpectedException extends BaseException {
    public UnexpectedException(Throwable cause) {
        super(
                ErrorCatalog.INTERNAL_SERVER_ERROR.getErrorCode(),
                ErrorCatalog.INTERNAL_SERVER_ERROR.getTitle(),
                ErrorCatalog.INTERNAL_SERVER_ERROR.getMessage(),
                ErrorCatalog.INTERNAL_SERVER_ERROR.getStatus(),
                ErrorCatalog.INTERNAL_SERVER_ERROR.getErrors()
        );
        initCause(cause);
    }
}