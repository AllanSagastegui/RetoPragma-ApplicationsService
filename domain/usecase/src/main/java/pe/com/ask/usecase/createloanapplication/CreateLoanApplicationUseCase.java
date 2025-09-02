package pe.com.ask.usecase.createloanapplication;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.model.loanwithclient.gateways.UserIdentityGateway;
import pe.com.ask.usecase.exception.UnauthorizedLoanApplicationException;
import pe.com.ask.usecase.getdefaultstatus.GetDefaultStatusUseCase;
import pe.com.ask.usecase.persistloanapplication.PersistLoanApplicationUseCase;
import pe.com.ask.usecase.validateloanamount.ValidateLoanAmountUseCase;
import pe.com.ask.usecase.validateloantype.ValidateLoanTypeUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateLoanApplicationUseCase {

    private final ValidateLoanTypeUseCase validateLoanTypeUseCase;
    private final ValidateLoanAmountUseCase validateLoanAmountUseCase;
    private final GetDefaultStatusUseCase getDefaultStatusUseCase;
    private final PersistLoanApplicationUseCase persistLoanApplicationUseCase;
    private final TransactionalGateway transactionalGateway;
    private final UserIdentityGateway userIdentityGateway;
    private final CustomLogger logger;

    public Mono<LoanApplicationData> createLoanApplication(LoanApplication loanApplication, String loanTypeName) {
        logger.trace("Start createLoanApplication flow", loanApplication.getDni());

        return userIdentityGateway.getCurrentUserDni()
                .zipWith(userIdentityGateway.getCurrentUserId())
                .flatMap(tuple -> {
                    String dniFromToken = tuple.getT1();
                    String userIdFromToken = tuple.getT2();

                    if (!loanApplication.getDni().equals(dniFromToken)) {
                        logger.trace("DNI mismatch in CreateLoanApplicationUseCase",
                                loanApplication.getDni(), dniFromToken);
                        return Mono.error(new UnauthorizedLoanApplicationException());
                    }

                    loanApplication.setUserId(UUID.fromString(userIdFromToken));

                    return transactionalGateway.executeInTransaction(
                            validateLoanTypeUseCase.execute(loanTypeName)
                                    .flatMap(type -> validateLoanAmountUseCase.execute(loanApplication, type)
                                            .flatMap(validLoan -> getDefaultStatusUseCase.execute()
                                                    .flatMap(status -> {
                                                        validLoan.setIdStatus(status.getIdStatus());
                                                        return persistLoanApplicationUseCase.execute(validLoan)
                                                                .map(app -> LoanApplicationData.builder()
                                                                        .idLoanApplication(app.getIdLoanApplication())
                                                                        .amount(app.getAmount())
                                                                        .term(app.getTerm())
                                                                        .email(app.getEmail())
                                                                        .dni(app.getDni())
                                                                        .status(status.getName())
                                                                        .loanType(type.getName())
                                                                        .build());
                                                    })))
                    );
                })
                .doOnError(err -> logger.trace("Error in CreateLoanApplicationUseCase",
                        loanApplication.getDni(), err.getMessage()));
    }
}