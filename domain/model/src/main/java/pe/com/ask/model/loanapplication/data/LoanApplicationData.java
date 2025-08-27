package pe.com.ask.model.loanapplication.data;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanApplicationData {
    private UUID idLoanApplication;
    private BigDecimal amount;
    private int term;
    private String email;
    private String dni;

    private String status;
    private String loanType;
}