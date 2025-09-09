package pe.com.ask.usecase.exception;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class LoanApplicationNotFoundException extends BaseException {
    public LoanApplicationNotFoundException() {
        super(
                ErrorCatalog.LOAN_APPLICATION_NOT_FOUND.getErrorCode(),
                ErrorCatalog.LOAN_APPLICATION_NOT_FOUND.getTitle(),
                ErrorCatalog.LOAN_APPLICATION_NOT_FOUND.getMessage(),
                ErrorCatalog.LOAN_APPLICATION_NOT_FOUND.getStatus(),
                ErrorCatalog.LOAN_APPLICATION_NOT_FOUND.getErrors()
        );
    }
}