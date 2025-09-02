package pe.com.ask.consumer.dto.response;

import java.math.BigDecimal;

public record GetAllClientsResponse(
        String name,
        String lastName,
        String dni,
        String email,
        BigDecimal baseSalary
) { }