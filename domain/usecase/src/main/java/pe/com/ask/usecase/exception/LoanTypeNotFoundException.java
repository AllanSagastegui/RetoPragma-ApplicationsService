package pe.com.ask.usecase.exception;

import pe.com.ask.usecase.utils.errors.ErrorCatalog;

public class LoanTypeNotFoundException extends BaseException {
    public LoanTypeNotFoundException() {
        super(
                ErrorCatalog.LOAN_TYPE_NOT_FOUND.getErrorCode(),
                ErrorCatalog.LOAN_TYPE_NOT_FOUND.getTitle(),
                ErrorCatalog.LOAN_TYPE_NOT_FOUND.getMessage(),
                ErrorCatalog.LOAN_TYPE_NOT_FOUND.getStatus(),
                ErrorCatalog.LOAN_TYPE_NOT_FOUND.getErrors()
        );
    }
}