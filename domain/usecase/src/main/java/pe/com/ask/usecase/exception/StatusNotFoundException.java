package pe.com.ask.usecase.exception;

import pe.com.ask.usecase.utils.errors.ErrorCatalog;

public class StatusNotFoundException extends BaseException {
    public StatusNotFoundException(){
        super(
                ErrorCatalog.STATUS_NOT_FOUND.getErrorCode(),
                ErrorCatalog.STATUS_NOT_FOUND.getTitle(),
                ErrorCatalog.STATUS_NOT_FOUND.getMessage(),
                ErrorCatalog.STATUS_NOT_FOUND.getStatus(),
                ErrorCatalog.STATUS_NOT_FOUND.getErrors()
        );
    }
}