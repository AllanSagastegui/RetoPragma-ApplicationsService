package pe.com.ask.model.loanwithclient;

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
public class ClientSnapshot {
    private UUID id;
    private String name;
    private String lastName;
    private String dni;
    private String email;
    private BigDecimal baseSalary;
}