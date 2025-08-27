package pe.com.ask.usecase.createloanapplication;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.exception.LoanAmountOutOfRangeException;
import pe.com.ask.usecase.exception.LoanTypeNotFound;
import pe.com.ask.usecase.exception.StatusNotFound;
import pe.com.ask.usecase.utils.STATUS_DEFAULT;
import pe.com.ask.usecase.utils.logmessages.LoanApplicationLog;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StatusRepository statusRepository;
    private final TransactionalGateway transactionalGateway;
    private final CustomLogger logger;

    public Mono<LoanApplicationData> createLoanApplication(LoanApplication loanApplication, String loanTypeName) {
        logger.trace(LoanApplicationLog.START_FLOW, loanApplication.getDni());

        return transactionalGateway.executeInTransaction(
                loanTypeRepository.findByName(loanTypeName)
                        .doOnNext(type -> logger.trace(LoanApplicationLog.LOAN_TYPE_FOUND, type.getName()))
                        .switchIfEmpty(Mono.defer(() -> {
                            logger.trace(LoanApplicationLog.LOAN_TYPE_NOT_FOUND, loanTypeName);
                            return Mono.error(new LoanTypeNotFound());
                        }))
                        .flatMap(type -> {
                            boolean validAmount = loanApplication.getAmount().compareTo(type.getMinimumAmount()) >= 0 &&
                                    loanApplication.getAmount().compareTo(type.getMaximumAmount()) <= 0;

                            if (!validAmount) {
                                logger.trace(LoanApplicationLog.LOAN_AMOUNT_OUT_OF_RANGE,
                                        loanApplication.getAmount(), type.getName());
                                return Mono.error(new LoanAmountOutOfRangeException());
                            }

                            loanApplication.setIdLoanType(type.getIdLoanType());
                            return statusRepository.findByName(STATUS_DEFAULT.PENDING_REVIEW.getName())
                                    .switchIfEmpty(Mono.defer(() -> {
                                        logger.trace(LoanApplicationLog.STATUS_NOT_FOUND,
                                                STATUS_DEFAULT.PENDING_REVIEW.getName());
                                        return Mono.error(new StatusNotFound());
                                    }))
                                    .flatMap(status -> {
                                        loanApplication.setIdStatus(status.getIdStatus());
                                        return loanApplicationRepository.createLoanApplication(loanApplication)
                                                .doOnSuccess(app -> logger.trace(LoanApplicationLog.LOAN_APPLICATION_CREATED,
                                                        app.getIdLoanApplication()))
                                                .map(app -> LoanApplicationData.builder()
                                                        .idLoanApplication(app.getIdLoanApplication())
                                                        .amount(app.getAmount())
                                                        .term(app.getTerm())
                                                        .email(app.getEmail())
                                                        .dni(app.getDni())
                                                        .status(status.getName())
                                                        .loanType(type.getName())
                                                        .build());
                                    });
                        })
        ).doOnError(err -> logger.trace(LoanApplicationLog.ERROR_OCCURRED,
                loanApplication.getDni(), err.getMessage()));
    }
}