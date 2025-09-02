package pe.com.ask.usecase.getallloanapplicationunderreview;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.model.loanwithclient.Pageable;
import pe.com.ask.usecase.getallloanapplicationunderreview.getallloantypes.GetAllLoanTypesUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getallstatusprocess.GetAllStatusProcessUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getclientsbyids.GetClientsByIdsUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getloansandtotal.GetLoansAndTotalUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getstatusids.GetStatusIdsUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.loanprocess.LoanProcessUseCase;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GetAllLoanApplicationUnderReviewUseCase {

    private final GetStatusIdsUseCase getStatusIdsUseCase;
    private final GetLoansAndTotalUseCase getLoansAndTotalUseCase;
    private final GetAllStatusProcessUseCase getAllStatusProcessUseCase;
    private final GetClientsByIdsUseCase getClientsByIdsUseCase;
    private final GetAllLoanTypesUseCase getAllLoanTypesUseCase;
    private final LoanProcessUseCase loanProcessUseCase;
    private final CustomLogger logger;

    public Mono<Pageable<LoanWithClient>> execute(int page, int size, String statusFilter) {
        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.START_USE_CASE,
                page, size, statusFilter);

        return getStatusIdsUseCase.execute(statusFilter)
                .doOnNext(statusIds -> logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.STATUS_IDS_RETRIEVED, statusIds))
                .flatMap(statusIds -> getLoansAndTotalUseCase.execute(statusIds, page, size)
                        .doOnNext(tuple ->
                                logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.LOANS_AND_TOTAL_RETRIEVED, tuple.getT1(), tuple.getT2().size())
                        )
                        .flatMap(tuple -> {
                            Long total = tuple.getT1();
                            List<LoanApplication> loans = tuple.getT2();
                            List<UUID> userIds = loans.stream().map(LoanApplication::getUserId).toList();

                            logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.USER_IDS_EXTRACTED, userIds);

                            Mono<Map<UUID, String>> statusMapMono = getAllStatusProcessUseCase.execute()
                                    .doOnNext(map ->
                                            logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.STATUS_MAP_RETRIEVED, map.size())
                                    );

                            return Mono.zip(
                                    getClientsByIdsUseCase.execute(userIds)
                                            .doOnNext(clients ->
                                                    logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.CLIENTS_RETRIEVED, clients.size())
                                            ),
                                    statusMapMono,
                                    getAllLoanTypesUseCase.execute()
                                            .doOnNext(types ->
                                                    logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.LOAN_TYPES_RETRIEVED, types.size())
                                            )
                            ).map(triple -> {
                                List<LoanWithClient> content = loanProcessUseCase.map(loans, triple.getT1(), triple.getT2(), triple.getT3());
                                logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.LOANS_MAPPED, content.size());

                                return Pageable.<LoanWithClient>builder()
                                        .content(content)
                                        .page(page)
                                        .size(size)
                                        .totalElements(total)
                                        .totalPages((int) Math.ceil((double) total / size))
                                        .build();
                            });
                        }))
                .doOnError(ex -> logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.ERROR_OCCURRED, ex.getMessage()));
    }
}