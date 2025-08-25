package pe.com.ask.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponseCreateLoanApplication(
        UUID idTransaction,
        BigDecimal amount,
        int term,
        String email,
        String dni,
        String status,
        String loanType
) { }