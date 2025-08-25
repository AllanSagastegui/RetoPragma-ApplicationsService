package pe.com.ask.api.mapper;

import org.mapstruct.Mapper;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.model.loanapplication.LoanApplication;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {
    default LoanApplication toDomain(CreateLoanApplicationDTO dto){
        return new LoanApplication(
                dto.idLoanApplication(),
                dto.amount(),
                dto.term(),
                dto.email(),
                dto.dni(),
                null,
                null
        );
    }
}