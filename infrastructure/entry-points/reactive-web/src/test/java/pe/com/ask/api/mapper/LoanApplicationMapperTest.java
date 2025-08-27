package pe.com.ask.api.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseCreateLoanApplication;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationMapperTest {

    private final LoanApplicationMapper mapper = new LoanApplicationMapper() {};

    @Test
    @DisplayName("Should map CreateLoanApplicationDTO to LoanApplication domain correctly")
    void testToDomain() {
        CreateLoanApplicationDTO dto = new CreateLoanApplicationDTO(
                new BigDecimal("10000.00"),
                24,
                "test@example.com",
                "12345678",
                "Préstamo Personal"
        );

        LoanApplication domain = mapper.toDomain(dto);

        assertNotNull(domain);
        assertNull(domain.getIdLoanApplication());
        assertEquals(dto.amount(), domain.getAmount());
        assertEquals(dto.term(), domain.getTerm());
        assertEquals(dto.email(), domain.getEmail());
        assertEquals(dto.dni(), domain.getDni());
        assertNull(domain.getIdStatus());
        assertNull(domain.getIdLoanType());
    }

    @Test
    @DisplayName("Should map LoanApplicationDTO to ResponseCreateLoanApplication correctly")
    void testToResponse() {
        UUID id = UUID.randomUUID();
        LoanApplicationData loanApplicationData = new LoanApplicationData(
                id,
                new BigDecimal("15000.00"),
                12,
                "user@test.com",
                "87654321",
                "APPROVED",
                "Hipotecario"
        );

        ResponseCreateLoanApplication response = mapper.toResponse(loanApplicationData);

        assertNotNull(response);
        assertEquals(id, response.idTransaction());
        assertEquals(loanApplicationData.getAmount(), response.amount());
        assertEquals(loanApplicationData.getTerm(), response.term());
        assertEquals(loanApplicationData.getEmail(), response.email());
        assertEquals(loanApplicationData.getDni(), response.dni());
        assertEquals(loanApplicationData.getStatus(), response.status());
        assertEquals(loanApplicationData.getLoanType(), response.loanType());
    }

    @Test
    @DisplayName("Should return null when mapping null LoanApplicationDTO")
    void testToResponseWithNull() {
        assertNull(mapper.toResponse(null));
    }

}