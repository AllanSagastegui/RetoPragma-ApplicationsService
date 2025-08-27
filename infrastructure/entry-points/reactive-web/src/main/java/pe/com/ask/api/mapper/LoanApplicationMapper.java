package pe.com.ask.api.mapper;

import org.mapstruct.Mapper;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseCreateLoanApplication;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {
    default LoanApplication toDomain(CreateLoanApplicationDTO dto){
        return new LoanApplication(
                null,
                dto.amount(),
                dto.term(),
                dto.email(),
                dto.dni(),
                null,
                null
        );
    }

    default ResponseCreateLoanApplication toResponse(LoanApplicationData loanApplication)  {
        if (loanApplication == null) return null;
        return new ResponseCreateLoanApplication(
                loanApplication.getIdLoanApplication(),
                loanApplication.getAmount(),
                loanApplication.getTerm(),
                loanApplication.getEmail(),
                loanApplication.getDni(),
                loanApplication.getStatus(),
                loanApplication.getLoanType()
        );
    }
}