package pe.com.ask.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table("loan_application")
public class LoanApplicationEntity {
    @Id
    @Column("id")
    private UUID idLoanApplication;

    @Column("amount")
    private BigDecimal amount;

    @Column("term")
    private int term;

    @Column("email")
    private String email;

    @Column("dni")
    private String dni;

    @Column("id_status")
    private UUID idStatus;

    @Column("id_loan_type")
    private UUID idLoanType;

    @Column("id_user")
    private UUID userId;
}