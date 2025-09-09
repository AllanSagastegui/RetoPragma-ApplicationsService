package pe.com.ask.usecase.getallloanapplicationunderreview;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.model.loanwithclient.Pageable;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.usecase.getallloanapplicationunderreview.getallloantypes.GetAllLoanTypesUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getallstatusprocess.GetAllStatusProcessUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getclientsbyids.GetClientsByIdsUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getloansandtotal.GetLoansAndTotalUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getstatusids.GetStatusIdsUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.loanprocess.LoanProcessUseCase;
import pe.com.ask.usecase.utils.logmessages.GetAllLoanApplicationUnderReviewUseCaseLog;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

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
        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.START_USE_CASE, page, size, statusFilter);

        return getStatusIds(statusFilter)
                .flatMap(statusIds -> fetchLoansAndTotal(statusIds, page, size))
                .flatMap(tuple -> buildPageable(tuple, page, size))
                .doOnError(ex -> logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.ERROR_OCCURRED, ex.getMessage()));
    }

    private Mono<List<UUID>> getStatusIds(String statusFilter) {
        return getStatusIdsUseCase.execute(statusFilter)
                .doOnNext(statusIds ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.STATUS_IDS_RETRIEVED, statusIds));
    }

    private Mono<Tuple2<Long, List<LoanApplication>>> fetchLoansAndTotal(List<UUID> statusIds, int page, int size) {
        return getLoansAndTotalUseCase.execute(statusIds, page, size)
                .doOnNext(tuple ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.LOANS_AND_TOTAL_RETRIEVED,
                                tuple.getT1(), tuple.getT2().size()));
    }

    private Mono<Pageable<LoanWithClient>> buildPageable(Tuple2<Long, List<LoanApplication>> tuple, int page, int size) {
        Long total = tuple.getT1();
        List<LoanApplication> loans = tuple.getT2();
        List<UUID> userIds = extractUserIds(loans);

        Mono<Map<UUID, String>> statusMapMono = getStatusMap();
        Mono<Map<UUID, ClientSnapshot>> clientsMono = getClients(userIds);
        Mono<Map<UUID, LoanType>> loanTypesMono = getLoanTypes();

        return Mono.zip(clientsMono, statusMapMono, loanTypesMono)
                .map(triple -> mapToPageable(loans, triple, total, page, size));
    }

    private List<UUID> extractUserIds(List<LoanApplication> loans) {
        List<UUID> userIds = loans.stream().map(LoanApplication::getUserId).toList();
        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.USER_IDS_EXTRACTED, userIds);
        return userIds;
    }

    private Mono<Map<UUID, String>> getStatusMap() {
        return getAllStatusProcessUseCase.execute()
                .doOnNext(map ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.STATUS_MAP_RETRIEVED, map.size()));
    }

    private Mono<Map<UUID, ClientSnapshot>> getClients(List<UUID> userIds) {
        return getClientsByIdsUseCase.execute(userIds)
                .doOnNext(clients ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.CLIENTS_RETRIEVED, clients.size()));
    }

    private Mono<Map<UUID, LoanType>> getLoanTypes() {
        return getAllLoanTypesUseCase.execute()
                .doOnNext(types ->
                        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.LOAN_TYPES_RETRIEVED, types.size()));
    }

    private Pageable<LoanWithClient> mapToPageable(List<LoanApplication> loans,
                                                   Tuple3<Map<UUID, ClientSnapshot>, Map<UUID, String>, Map<UUID, LoanType>> triple,
                                                   Long total, int page, int size) {
        List<LoanWithClient> content = loanProcessUseCase.map(loans, triple.getT1(), triple.getT2(), triple.getT3());
        logger.trace(GetAllLoanApplicationUnderReviewUseCaseLog.LOANS_MAPPED, content.size());

        return Pageable.<LoanWithClient>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / size))
                .build();
    }
}