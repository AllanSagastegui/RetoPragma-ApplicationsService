package pe.com.ask.usecase.getallloanapplicationunderreview.getallloantypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllLoanTypesUseCaseTest {

    @Mock private LoanTypeRepository loanTypeRepository;
    @Mock private CustomLogger logger;

    private GetAllLoanTypesUseCase useCase;

    private LoanType loanType1;
    private LoanType loanType2;

    @BeforeEach
    void setUp() {
        useCase = new GetAllLoanTypesUseCase(loanTypeRepository, logger);

        loanType1 = LoanType.builder()
                .idLoanType(UUID.randomUUID())
                .name("Personal Loan")
                .minimumAmount(null)
                .maximumAmount(null)
                .build();

        loanType2 = LoanType.builder()
                .idLoanType(UUID.randomUUID())
                .name("Business Loan")
                .minimumAmount(null)
                .maximumAmount(null)
                .build();
    }

    @Test
    void execute_shouldReturnMapOfLoanTypes_whenLoanTypesExist() {
        when(loanTypeRepository.findAll()).thenReturn(Flux.just(loanType1, loanType2));

        StepVerifier.create(useCase.execute())
                .expectNextMatches(map -> map.size() == 2 &&
                        map.containsKey(loanType1.getIdLoanType()) &&
                        map.containsKey(loanType2.getIdLoanType()))
                .verifyComplete();

        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_LOAN_TYPES_EXECUTING);
        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_LOAN_TYPES_RETRIEVED,
                Set.of(loanType1.getIdLoanType(), loanType2.getIdLoanType()));
        verify(logger, never()).error(anyString(), any());
    }

    @Test
    void execute_shouldReturnError_whenRepositoryFails() {
        when(loanTypeRepository.findAll()).thenReturn(Flux.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute())
                .expectErrorMessage("DB error")
                .verify();

        verify(logger, times(1)).trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_LOAN_TYPES_EXECUTING);
        verify(logger, times(1)).error(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_LOAN_TYPES_ERROR, "DB error");
    }
}