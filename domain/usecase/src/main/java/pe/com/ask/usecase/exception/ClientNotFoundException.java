package pe.com.ask.usecase.exception;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class ClientNotFoundException extends BaseException {
    public ClientNotFoundException() {
        super(
                ErrorCatalog.CLIENT_NOT_FOUND.getErrorCode(),
                ErrorCatalog.CLIENT_NOT_FOUND.getTitle(),
                ErrorCatalog.CLIENT_NOT_FOUND.getMessage(),
                ErrorCatalog.CLIENT_NOT_FOUND.getStatus(),
                ErrorCatalog.CLIENT_NOT_FOUND.getErrors()
        );
    }
}