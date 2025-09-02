package pe.com.ask.usecase.exception;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class LoanAmountOutOfRangeException extends BaseException {
    public LoanAmountOutOfRangeException() {
        super(
                ErrorCatalog.LOAN_AMOUNT_OUT_OF_RANGE.getErrorCode(),
                ErrorCatalog.LOAN_AMOUNT_OUT_OF_RANGE.getTitle(),
                ErrorCatalog.LOAN_AMOUNT_OUT_OF_RANGE.getMessage(),
                ErrorCatalog.LOAN_AMOUNT_OUT_OF_RANGE.getStatus(),
                ErrorCatalog.LOAN_AMOUNT_OUT_OF_RANGE.getErrors()
        );
    }
}