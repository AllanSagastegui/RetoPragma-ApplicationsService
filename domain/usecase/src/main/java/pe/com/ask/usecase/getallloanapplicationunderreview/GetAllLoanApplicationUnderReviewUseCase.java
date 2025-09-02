package pe.com.ask.usecase.getallloanapplicationunderreview;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.loanapplication.LoanApplication;
import pe.com.ask.model.loanapplication.data.LoanApplicationData;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loantype.LoanType;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.model.loanwithclient.ClientSnapshot;
import pe.com.ask.model.loanwithclient.LoanWithClient;
import pe.com.ask.model.loanwithclient.Pageable;
import pe.com.ask.model.loanwithclient.gateways.ClientSnapshotRepository;
import pe.com.ask.model.status.Status;
import pe.com.ask.model.status.gateways.StatusRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GetAllLoanApplicationUnderReviewUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final StatusRepository statusRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final ClientSnapshotRepository clientSnapshotRepository;
    private final CustomLogger logger;

    public Mono<Pageable<LoanWithClient>> execute(int page, int size) {
        List<String> reviewStatusNames = List.of(
                "Pendiente de revisión",
                "Rechazada"
                //"Manual review"
        );

        return statusRepository.findIdsByNames(reviewStatusNames)
                .collectList()
                .flatMap(statusIds -> {
                    int offset = page * size;
                    Mono<Long> total = loanApplicationRepository.countLoansByIdStatus(statusIds);
                    Mono<List<LoanApplication>> loanApplicationMono = loanApplicationRepository
                            .findLoansByIdStatus(statusIds, offset, size)
                            .collectList();
                    return Mono.zip(total, loanApplicationMono);
                })
                .flatMap(tuple -> {
                    Long total = tuple.getT1();
                    List<LoanApplication> loanApplications = tuple.getT2();
                    List<UUID> userIds = loanApplications.stream()
                            .map(LoanApplication::getUserId)
                            .toList();

                    Mono<Map<UUID, ClientSnapshot>> clientSnapshotsMono = clientSnapshotRepository.findClientsByIds(userIds)
                            .collectMap(ClientSnapshot::getId);

                    Mono<Map<UUID, String>> statusNamesMono = statusRepository.findAll()
                            .collectMap(Status::getIdStatus, Status::getName);

                    Mono<Map<UUID, LoanType>> loanTypeNamesMono = loanTypeRepository.findAll()
                            .collectMap(LoanType::getIdLoanType);

                    return Mono.zip(clientSnapshotsMono, statusNamesMono, loanTypeNamesMono)
                            .map(triple -> {
                                Map<UUID, ClientSnapshot> clientMap = triple.getT1();
                                Map<UUID, String> statusMap = triple.getT2();
                                Map<UUID, LoanType> loanTypeMap = triple.getT3();

                                List<LoanWithClient> content = loanApplications.stream()
                                        .map(loanApplication -> {
                                            LoanApplicationData loanApplicationData = LoanApplicationData.builder()
                                                    .idLoanApplication(loanApplication.getIdLoanApplication())
                                                    .amount(loanApplication.getAmount())
                                                    .term(loanApplication.getTerm())
                                                    .email(loanApplication.getEmail())
                                                    .dni(loanApplication.getDni())
                                                    .status(statusMap.get(loanApplication.getIdStatus()))
                                                    .loanType(loanTypeMap.get(loanApplication.getIdLoanType()).getName())
                                                    .build();

                                            return LoanWithClient.builder()
                                                    .loanApplicationData(loanApplicationData)
                                                    .clientSnapshot(clientMap.get(loanApplication.getUserId()))
                                                    .interestRate(loanTypeMap.get(loanApplication.getIdLoanType()).getInterestRate())
                                                    .totalMonthlyDebt(calculateMonthlyPayment(
                                                            loanApplication.getAmount(),
                                                            loanTypeMap.get(loanApplication.getIdLoanType()).getInterestRate(),
                                                            loanApplication.getTerm()
                                                    ))
                                                    .approvedLoans(0)
                                                    .build();
                                        })
                                        .toList();

                                return Pageable.<LoanWithClient>builder()
                                        .content(content)
                                        .page(page)
                                        .size(size)
                                        .totalElements(total)
                                        .totalPages((int) Math.ceil((double) total / size))
                                        .build();
                            });
                });
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRate, int termMonths) {
        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal numerator = monthlyRate.multiply((monthlyRate.add(BigDecimal.ONE)).pow(termMonths));
        BigDecimal denominator = ((monthlyRate.add(BigDecimal.ONE)).pow(termMonths)).subtract(BigDecimal.ONE);

        return principal.multiply(numerator.divide(denominator, 10, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }
}