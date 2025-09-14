package pe.com.ask.model.loanapplication.gateways;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface UpdateReportsSQSGateway {
    Mono<Void> updateReports(String status, BigDecimal amount);
}