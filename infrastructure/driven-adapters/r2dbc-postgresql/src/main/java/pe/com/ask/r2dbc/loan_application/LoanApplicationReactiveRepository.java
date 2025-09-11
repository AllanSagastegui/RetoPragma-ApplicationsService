package pe.com.ask.r2dbc.loan_application;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.ask.r2dbc.entity.LoanApplicationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, UUID>,
        ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

    @Query("""
        SELECT * FROM loan_application
        WHERE id_status IN (:statusIds)
        OFFSET :offset
        LIMIT :limit;
    """)
    Flux<LoanApplicationEntity> findAllByIdStatusIn(List<UUID> statusIds, int offset, int limit);

    @Query("""
        SELECT COUNT(*) FROM loan_application
        WHERE id_status IN (:statusIds)
    """)
    Mono<Long> countByIdStatusIn(List<UUID> statusIds);

    @Query("""
        SELECT la.*
        FROM loan_application la
        JOIN status s ON la.id_status = s.id_status
        WHERE la.id_user = :userId
        AND s.name = 'Aprobada'
    """)
    Flux<LoanApplicationEntity> findAllApprovedLoansApplicationsByUserId(UUID userId);
}