package pe.com.ask.usecase.getallloanapplicationunderreview.loanprocess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanProcessUseCaseTest {

    @Mock private CustomLogger logger;

    private LoanProcessUseCase useCase;

    private LoanApplication loan1;
    private LoanApplication loan2;
    private ClientSnapshot client1;
    private ClientSnapshot client2;
    private LoanType loanType1;
    private LoanType loanType2;

    @BeforeEach
    void setUp() {
        useCase = new LoanProcessUseCase(logger);

        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        UUID loanTypeId1 = UUID.randomUUID();
        UUID loanTypeId2 = UUID.randomUUID();
        UUID statusId1 = UUID.randomUUID();
        UUID statusId2 = UUID.randomUUID();

        loan1 = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .userId(userId1)
                .idLoanType(loanTypeId1)
                .idStatus(statusId1)
                .amount(new BigDecimal("1000"))
                .term(12)
                .dni("12345678")
                .email("alice@test.com")
                .build();

        loan2 = LoanApplication.builder()
                .idLoanApplication(UUID.randomUUID())
                .userId(userId2)
                .idLoanType(loanTypeId2)
                .idStatus(statusId2)
                .amount(new BigDecimal("2000"))
                .term(24)
                .dni("87654321")
                .email("bob@test.com")
                .build();

        client1 = ClientSnapshot.builder().id(userId1).name("Alice").dni("12345678").build();
        client2 = ClientSnapshot.builder().id(userId2).name("Bob").dni("87654321").build();

        loanType1 = LoanType.builder().idLoanType(loanTypeId1).name("Personal Loan").interestRate(new BigDecimal("12")).build();
        loanType2 = LoanType.builder().idLoanType(loanTypeId2).name("Business Loan").interestRate(BigDecimal.ZERO).build();
    }

    @Test
    @DisplayName("Should correctly map loans to LoanWithClient and calculate total monthly debt")
    void map_shouldReturnLoanWithClientList() {
        List<LoanWithClient> result = useCase.map(
                List.of(loan1, loan2),
                Map.of(client1.getId(), client1, client2.getId(), client2),
                Map.of(loan1.getIdStatus(), "Pendiente", loan2.getIdStatus(), "Rechazada"),
                Map.of(loanType1.getIdLoanType(), loanType1, loanType2.getIdLoanType(), loanType2)
        );

        assertEquals(2, result.size());

        LoanWithClient l1 = result.get(0);
        LoanWithClient l2 = result.get(1);

        assertEquals(client1, l1.getClientSnapshot());
        assertEquals("Personal Loan", l1.getLoanApplicationData().getLoanType());
        assertEquals("Pendiente", l1.getLoanApplicationData().getStatus());
        assertEquals(new BigDecimal("88.85"), l1.getTotalMonthlyDebt());

        assertEquals(client2, l2.getClientSnapshot());
        assertEquals("Business Loan", l2.getLoanApplicationData().getLoanType());
        assertEquals("Rechazada", l2.getLoanApplicationData().getStatus());
        assertEquals(new BigDecimal("83.33"), l2.getTotalMonthlyDebt()); // 2000/24

        verify(logger, times(1)).trace(
                eq(GetAllLoanApplicationUnderReviewUseCaseLog.LOAN_PROCESS_EXECUTING),
                eq(2)
        );
        verify(logger, times(1)).trace(
                eq(GetAllLoanApplicationUnderReviewUseCaseLog.LOAN_PROCESS_COMPLETED),
                eq(2)
        );
    }
}