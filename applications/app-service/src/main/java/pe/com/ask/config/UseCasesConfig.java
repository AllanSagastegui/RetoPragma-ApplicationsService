package pe.com.ask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.model.loanwithclient.gateways.ClientSnapshotRepository;
import pe.com.ask.model.loanwithclient.gateways.UserIdentityGateway;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.createloanapplication.CreateLoanApplicationUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.GetAllLoanApplicationUnderReviewUseCase;
import pe.com.ask.usecase.getdefaultstatus.GetDefaultStatusUseCase;
import pe.com.ask.usecase.persistloanapplication.PersistLoanApplicationUseCase;
import pe.com.ask.usecase.validateloanamount.ValidateLoanAmountUseCase;
import pe.com.ask.usecase.validateloantype.ValidateLoanTypeUseCase;

@Configuration
@ComponentScan(basePackages = "pe.com.ask.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    ValidateLoanTypeUseCase  validateLoanTypeUseCase(
            LoanTypeRepository loanTypeRepository,
            CustomLogger logger
    ){
        return new  ValidateLoanTypeUseCase(loanTypeRepository, logger);
    }

    @Bean
    ValidateLoanAmountUseCase  validateLoanAmountUseCase(CustomLogger logger){
        return new  ValidateLoanAmountUseCase(logger);
    }

    @Bean
    GetDefaultStatusUseCase getDefaultStatusUseCase(
            StatusRepository statusRepository,
            CustomLogger logger
    ){
        return new  GetDefaultStatusUseCase(statusRepository, logger);
    }

    @Bean
    PersistLoanApplicationUseCase  persistLoanApplicationUseCase(
            LoanApplicationRepository loanApplicationRepository,
            CustomLogger logger
    ){
        return new  PersistLoanApplicationUseCase(loanApplicationRepository, logger);
    }

    @Bean
    CreateLoanApplicationUseCase createLoanApplicationUseCase(
            ValidateLoanTypeUseCase validateLoanTypeUseCase,
            ValidateLoanAmountUseCase validateLoanAmountUseCase,
            GetDefaultStatusUseCase getDefaultStatusUseCase,
            PersistLoanApplicationUseCase persistLoanApplicationUseCase,
            TransactionalGateway transactionalGateway,
            UserIdentityGateway userIdentityGateway,
            CustomLogger logger
    ) {
        return new CreateLoanApplicationUseCase(
                validateLoanTypeUseCase,
                validateLoanAmountUseCase,
                getDefaultStatusUseCase,
                persistLoanApplicationUseCase,
                transactionalGateway,
                userIdentityGateway,
                logger
        );
    }

    @Bean
    GetAllLoanApplicationUnderReviewUseCase getAllLoanApplicationUnderReviewUseCase(
            LoanApplicationRepository loanApplicationRepository,
            StatusRepository statusRepository,
            LoanTypeRepository loanTypeRepository,
            ClientSnapshotRepository clientSnapshotRepository,
            CustomLogger logger
    ) {
        return new  GetAllLoanApplicationUnderReviewUseCase(
                loanApplicationRepository,
                statusRepository,
                loanTypeRepository,
                clientSnapshotRepository,
                logger
        );
    }
}