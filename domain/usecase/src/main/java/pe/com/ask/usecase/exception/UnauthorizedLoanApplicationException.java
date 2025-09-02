package pe.com.ask.usecase.exception;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class UnauthorizedLoanApplicationException extends BaseException {
    public UnauthorizedLoanApplicationException() {
        super(
                ErrorCatalog.DNI_MISMATCH.getErrorCode(),
                ErrorCatalog.DNI_MISMATCH.getTitle(),
                ErrorCatalog.DNI_MISMATCH.getMessage(),
                ErrorCatalog.DNI_MISMATCH.getStatus(),
                ErrorCatalog.DNI_MISMATCH.getErrors()
        );
    }
}