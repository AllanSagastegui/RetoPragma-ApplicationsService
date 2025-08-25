package pe.com.ask.usecase.createloanapplication;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.dto.LoanApplicationDTO;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.exception.LoanAmountOutOfRangeException;
import pe.com.ask.usecase.exception.LoanTypeNotFound;
import pe.com.ask.usecase.exception.StatusNotFound;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StatusRepository statusRepository;
    private final TransactionalGateway transactionalGateway;
    private final CustomLogger logger;

    public Mono<LoanApplicationDTO> createLoanApplication(LoanApplication loanApplication, String loanTypeName) {
        logger.trace("Start create loan application flow for DNI: {}", loanApplication.getDni());
        return transactionalGateway.executeInTransaction(
                loanTypeRepository.findByName(loanTypeName)
                        .switchIfEmpty(Mono.error(new LoanTypeNotFound()))
                        .filter(type -> loanApplication.getAmount().compareTo(type.getMinimumAmount()) >= 0 &&
                                loanApplication.getAmount().compareTo(type.getMaximumAmount()) <= 0)
                        .switchIfEmpty(Mono.error(new LoanAmountOutOfRangeException()))
                        .flatMap(type -> {
                            loanApplication.setIdLoanType(type.getIdLoanType());
                            return statusRepository.findByName("Pendiente de revisión")
                                    .switchIfEmpty(Mono.error(new StatusNotFound()))
                                    .flatMap(status -> {
                                        loanApplication.setIdStatus(status.getIdStatus());
                                        return loanApplicationRepository.createLoanApplication(loanApplication)
                                                .map(app -> LoanApplicationDTO.builder()
                                                        .idLoanApplication(app.getIdLoanApplication())
                                                        .amount(app.getAmount())
                                                        .term(app.getTerm())
                                                        .email(app.getEmail())
                                                        .dni(app.getDni())
                                                        .status(status.getName())
                                                        .loanType(type.getName())
                                                        .build()
                                                );
                                    });
                        })
        ).doOnError(err -> logger.trace("Error creating loan application for DNI {}: {}", loanApplication.getDni(), err.getMessage()));
    }
}