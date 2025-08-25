package pe.com.ask.api;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.CreateLoanApplicationDTO;
import pe.com.ask.api.dto.response.ResponseCreateLoanApplication;
import pe.com.ask.api.exception.model.UnexpectedException;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.LoanApplicationMapper;
import pe.com.ask.usecase.createloanapplication.CreateLoanApplicationUseCase;
import pe.com.ask.usecase.exception.BaseException;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class LoanApplicationHandler {

    private final LoanApplicationMapper mapper;
    private final ValidationService validator;

    private final CreateLoanApplicationUseCase  createLoanApplicationUseCase;


    public Mono<ServerResponse> listenPOSTCreateLoanApplicationUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateLoanApplicationDTO.class)
                .flatMap(validator::validate)
                .flatMap(dto -> {
                    var domain = mapper.toDomain(dto);
                    return createLoanApplicationUseCase.createLoanApplication(domain, dto.loanType());
                })
                .map(mapper::toResponse)
                .flatMap(response
                        -> ServerResponse.created(URI.create("/api/v1/solicitud/"))
                        .bodyValue(response))
                .onErrorResume(ex -> Mono.error(
                        ex instanceof BaseException ? ex : new UnexpectedException(ex)
                ));
    }

    public Mono<ResponseCreateLoanApplication> createLoanApplicationDoc(
            @RequestBody(description = "Loan Application - Data required to create a new loan application")
            CreateLoanApplicationDTO dto) {
        return Mono.empty();
    }
}
