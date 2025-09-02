package pe.com.ask.usecase.createloanapplication.validateloanamount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.usecase.exception.LoanAmountOutOfRangeException;
import pe.com.ask.usecase.utils.logmessages.CreateLoanApplicationUseCaseLog;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateLoanAmountUseCaseTest {

    @Mock private CustomLogger logger;
    private ValidateLoanAmountUseCase validateLoanAmountUseCase;

    private LoanApplication loanApplication;
    private LoanType loanType;

    @BeforeEach
    void setUp() {
        validateLoanAmountUseCase = new ValidateLoanAmountUseCase(logger);

        loanApplication = new LoanApplication();
        loanApplication.setIdLoanApplication(UUID.randomUUID());
        loanApplication.setAmount(BigDecimal.valueOf(500));

        loanType = new LoanType();
        loanType.setIdLoanType(UUID.randomUUID());
        loanType.setMinimumAmount(BigDecimal.valueOf(100));
        loanType.setMaximumAmount(BigDecimal.valueOf(1000));
        loanType.setName("Personal Loan");
    }

    @Test
    void execute_shouldReturnLoanApplication_whenAmountIsValid() {
        StepVerifier.create(validateLoanAmountUseCase.execute(loanApplication, loanType))
                .expectNextMatches(app -> app.getIdLoanType().equals(loanType.getIdLoanType()))
                .verifyComplete();

        verify(logger, never()).trace(any(), any(), any());
    }

    @Test
    void execute_shouldReturnError_whenAmountIsOutOfRange() {
        loanApplication.setAmount(BigDecimal.valueOf(2000));

        StepVerifier.create(validateLoanAmountUseCase.execute(loanApplication, loanType))
                .expectError(LoanAmountOutOfRangeException.class)
                .verify();

        verify(logger, times(1))
                .trace(eq(CreateLoanApplicationUseCaseLog.LOAN_AMOUNT_OUT_OF_RANGE),
                        eq(loanApplication.getAmount()), eq(loanType.getName()));
    }

    @Test
    void execute_shouldReturnLoanApplication_whenAmountIsExactlyMinimum() {
        loanApplication.setAmount(loanType.getMinimumAmount());

        StepVerifier.create(validateLoanAmountUseCase.execute(loanApplication, loanType))
                .expectNextMatches(app -> app.getIdLoanType().equals(loanType.getIdLoanType()))
                .verifyComplete();

        verify(logger, never()).trace(any(), any(), any());
    }

    @Test
    void execute_shouldReturnLoanApplication_whenAmountIsExactlyMaximum() {
        loanApplication.setAmount(loanType.getMaximumAmount());

        StepVerifier.create(validateLoanAmountUseCase.execute(loanApplication, loanType))
                .expectNextMatches(app -> app.getIdLoanType().equals(loanType.getIdLoanType()))
                .verifyComplete();

        verify(logger, never()).trace(any(), any(), any());
    }

    @Test
    void execute_shouldReturnError_whenAmountIsBelowMinimum() {
        loanApplication.setAmount(loanType.getMinimumAmount().subtract(BigDecimal.ONE));

        StepVerifier.create(validateLoanAmountUseCase.execute(loanApplication, loanType))
                .expectError(LoanAmountOutOfRangeException.class)
                .verify();

        verify(logger, times(1))
                .trace(eq(CreateLoanApplicationUseCaseLog.LOAN_AMOUNT_OUT_OF_RANGE),
                        eq(loanApplication.getAmount()), eq(loanType.getName()));
    }

    @Test
    void execute_shouldReturnError_whenAmountIsAboveMaximum() {
        loanApplication.setAmount(loanType.getMaximumAmount().add(BigDecimal.ONE));

        StepVerifier.create(validateLoanAmountUseCase.execute(loanApplication, loanType))
                .expectError(LoanAmountOutOfRangeException.class)
                .verify();

        verify(logger, times(1))
                .trace(eq(CreateLoanApplicationUseCaseLog.LOAN_AMOUNT_OUT_OF_RANGE),
                        eq(loanApplication.getAmount()), eq(loanType.getName()));
    }

}
