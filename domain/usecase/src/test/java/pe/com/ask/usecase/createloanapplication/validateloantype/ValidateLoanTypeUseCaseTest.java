package pe.com.ask.usecase.createloanapplication.validateloantype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.usecase.exception.LoanTypeNotFoundException;
import pe.com.ask.usecase.utils.logmessages.CreateLoanApplicationUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateLoanTypeUseCaseTest {

    @Mock private LoanTypeRepository loanTypeRepository;
    @Mock private CustomLogger logger;

    private ValidateLoanTypeUseCase validateLoanTypeUseCase;
    private LoanType loanType;

    @BeforeEach
    void setUp() {
        validateLoanTypeUseCase = new ValidateLoanTypeUseCase(loanTypeRepository, logger);

        loanType = new LoanType();
        loanType.setIdLoanType(UUID.randomUUID());
        loanType.setName("Personal Loan");
    }

    @Test
    void execute_shouldReturnLoanType_whenTypeExists() {
        when(loanTypeRepository.findByName(anyString())).thenReturn(Mono.just(loanType));

        StepVerifier.create(validateLoanTypeUseCase.execute("Personal Loan"))
                .expectNextMatches(type -> type.getName().equals("Personal Loan"))
                .verifyComplete();

        verify(logger, times(1))
                .trace(eq(CreateLoanApplicationUseCaseLog.LOAN_TYPE_FOUND), eq("Personal Loan"));
        verify(logger, never()).trace(eq(CreateLoanApplicationUseCaseLog.LOAN_TYPE_NOT_FOUND), anyString());
    }

    @Test
    void execute_shouldReturnError_whenTypeDoesNotExist() {
        when(loanTypeRepository.findByName(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(validateLoanTypeUseCase.execute("Unknown Loan"))
                .expectError(LoanTypeNotFoundException.class)
                .verify();

        verify(logger, times(1))
                .trace(eq(CreateLoanApplicationUseCaseLog.LOAN_TYPE_NOT_FOUND), eq("Unknown Loan"));
        verify(logger, never()).trace(eq(CreateLoanApplicationUseCaseLog.LOAN_TYPE_FOUND), anyString());
    }
}