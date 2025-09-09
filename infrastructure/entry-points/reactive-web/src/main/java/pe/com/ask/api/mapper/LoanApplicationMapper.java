package pe.com.ask.api.mapper;

import org.mapstruct.Mapper;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseCreateLoanApplication;
import pe.com.ask.api.dto.response.ResponseGetLoanApplicationUnderReview;
import pe.com.ask.api.dto.response.ResponseUpdateLoanApplication;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.model.loanwithclient.LoanWithClient;

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

    default ResponseGetLoanApplicationUnderReview toResponseGetLoanApplicationUnderReview(LoanWithClient loanWithClient) {
        if (loanWithClient == null) return null;
        return new ResponseGetLoanApplicationUnderReview(
                loanWithClient.getLoanApplicationData().getIdLoanApplication(),
                loanWithClient.getClientSnapshot().getName(),
                loanWithClient.getClientSnapshot().getEmail(),
                loanWithClient.getLoanApplicationData().getAmount(),
                loanWithClient.getLoanApplicationData().getTerm(),
                loanWithClient.getClientSnapshot().getBaseSalary(),
                loanWithClient.getLoanApplicationData().getLoanType(),
                loanWithClient.getLoanApplicationData().getStatus(),
                loanWithClient.getInterestRate(),
                loanWithClient.getTotalMonthlyDebt(),
                loanWithClient.getApprovedLoans()
        );
    }

    default ResponseUpdateLoanApplication toResponseUpdateLoanApplication(LoanWithClient loanWithClient) {
        if (loanWithClient == null) return null;
        return new ResponseUpdateLoanApplication(
                loanWithClient.getClientSnapshot().getName(),
                loanWithClient.getClientSnapshot().getEmail(),
                loanWithClient.getLoanApplicationData().getAmount(),
                loanWithClient.getLoanApplicationData().getTerm(),
                loanWithClient.getClientSnapshot().getBaseSalary(),
                loanWithClient.getLoanApplicationData().getLoanType(),
                loanWithClient.getLoanApplicationData().getStatus(),
                loanWithClient.getInterestRate(),
                loanWithClient.getTotalMonthlyDebt(),
                loanWithClient.getApprovedLoans()
        );
    }
}