package pe.com.ask.usecase.getallloanapplicationunderreview.getallloantypes;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GetAllLoanTypesUseCase {

    private final LoanTypeRepository loanTypeRepository;
    private final CustomLogger logger;

    public Mono<Map<UUID, LoanType>> execute() {
        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_LOAN_TYPES_EXECUTING);

        return loanTypeRepository.findAll().collectMap(LoanType::getIdLoanType)
                .doOnNext(map ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_LOAN_TYPES_RETRIEVED, map.keySet())
                )
                .doOnError(e ->
                        logger.error(GetAllLoanApplicationUnderReviewUseCaseLog.GET_ALL_LOAN_TYPES_ERROR, e.getMessage())
                );
    }
}