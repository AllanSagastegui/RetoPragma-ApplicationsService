package pe.com.ask.usecase.getallloanapplicationunderreview.getloansandtotal;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetLoansAndTotalUseCase {
    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomLogger logger;

    public Mono<Tuple2<Long, List<LoanApplication>>> execute(
            List<UUID> statusIds,
            int page,
            int size) {

        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_EXECUTING, statusIds, page, size);

        if (statusIds == null || statusIds.isEmpty()) {
            logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_EMPTY_STATUS_IDS);
            return Mono.just(Tuples.of(0L, List.<LoanApplication>of()));
        }

        int offset = page * size;
        Mono<Long> total = loanApplicationRepository.countLoansByIdStatus(statusIds);
        Mono<List<LoanApplication>> loans = loanApplicationRepository.findLoansByIdStatus(statusIds, offset, size)
                .collectList();

        return Mono.zip(total, loans)
                .doOnNext(t ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_RETRIEVED, t.getT1(), t.getT2().size()))
                .doOnError(e -> logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.GET_LOANS_AND_TOTAL_ERROR, e.getMessage(), e.getMessage()));
    }
}