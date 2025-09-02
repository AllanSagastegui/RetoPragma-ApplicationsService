package pe.com.ask.model.loanwithclient;

import lombok.*;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanWithClient {
    private LoanApplicationData loanApplicationData;
    private ClientSnapshot clientSnapshot;

    private BigDecimal interestRate;
    private BigDecimal totalMonthlyDebt;
    private Integer approvedLoans;
}