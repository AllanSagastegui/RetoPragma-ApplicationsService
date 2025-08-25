package pe.com.ask.usecase.createloanapplication;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.loanapplication.LoanApplication;
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
    private final TransactionalGateway  transactionalGateway;
    private final CustomLogger  logger;

    public Mono<LoanApplication> createLoanApplication(LoanApplication loanApplication,  String loanTypeName) {
        logger.trace("Start create loan application flow for DNI: {}", loanApplication.getDni());

        return transactionalGateway.executeInTransaction(
                loanTypeRepository.findByName(loanTypeName)
                        .doOnSubscribe(sub -> logger.trace("Subscribed to find loan type: {}", loanTypeName))
                        .switchIfEmpty(Mono.defer(() -> {
                            logger.trace("Loan type not found: {}", loanTypeName);
                            return Mono.error(new LoanTypeNotFound());
                        }))
                        .filter(type -> loanApplication.getAmount().compareTo(type.getMinimumAmount()) >= 0 &&
                                                loanApplication.getAmount().compareTo(type.getMaximumAmount()) <= 0)
                        .switchIfEmpty(Mono.defer(() -> {
                            logger.trace("Loan amount {} is outside allowed range for loan type {}",
                                    loanApplication.getAmount(), loanTypeName);
                            return Mono.error(new LoanAmountOutOfRangeException());
                        }))
                        .flatMap(type -> {
                            logger.trace("Loan type found: {} with id {}", type.getName(), type.getIdLoanType());
                            loanApplication.setIdLoanType(type.getIdLoanType());

                            return statusRepository.findByName("Pendiente de revisión")
                                    .doOnSubscribe(sub -> logger.trace("Subscribed to find status: Pendiente de revisión"))
                                    .switchIfEmpty(Mono.defer(() -> {
                                        logger.trace("Status 'Pendiente de revisión' not found");
                                        return Mono.error(new StatusNotFound());
                                    }))
                                    .flatMap(status -> {
                                        logger.trace("Status found with id: {}", status.getIdStatus());
                                        loanApplication.setIdStatus(status.getIdStatus());

                                        return loanApplicationRepository.createLoanApplication(loanApplication)
                                                .doOnSuccess(app -> logger.trace("Loan application created successfully for DNI: {}", app.getDni()));
                                    });
                        })
        ).doOnError(err -> logger.trace("Error creating loan application for DNI {}: {}", loanApplication.getDni(), err.getMessage()));
    }
}