package pe.com.ask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.createloanapplication.CreateLoanApplicationUseCase;

@Configuration
@ComponentScan(basePackages = "pe.com.ask.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    CreateLoanApplicationUseCase createLoanApplicationUseCase(
            LoanApplicationRepository loanApplicationRepository,
            LoanTypeRepository loanTypeRepository,
            StatusRepository statusRepository,
            TransactionalGateway transactionalGateway,
            CustomLogger logger
    ) {
        return new CreateLoanApplicationUseCase(loanApplicationRepository, loanTypeRepository, statusRepository, transactionalGateway, logger);
    }
}