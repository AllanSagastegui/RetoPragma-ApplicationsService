package pe.com.ask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationSQSGateway;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.model.loanwithclient.gateways.ClientSnapshotRepository;
import pe.com.ask.model.loanwithclient.gateways.UserIdentityGateway;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.createloanapplication.CreateLoanApplicationUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.GetAllLoanApplicationUnderReviewUseCase;
import pe.com.ask.usecase.createloanapplication.getdefaultstatus.GetDefaultStatusUseCase;
import pe.com.ask.usecase.createloanapplication.persistloanapplication.PersistLoanApplicationUseCase;
import pe.com.ask.usecase.createloanapplication.validateloanamount.ValidateLoanAmountUseCase;
import pe.com.ask.usecase.createloanapplication.validateloantype.ValidateLoanTypeUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getallloantypes.GetAllLoanTypesUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getallstatusprocess.GetAllStatusProcessUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getclientsbyids.GetClientsByIdsUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getloansandtotal.GetLoansAndTotalUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.getstatusids.GetStatusIdsUseCase;
import pe.com.ask.usecase.getallloanapplicationunderreview.loanprocess.LoanProcessUseCase;
import pe.com.ask.usecase.updateloanapplication.UpdateLoanApplicationUseCase;
import pe.com.ask.usecase.updateloanapplication.buildloanwithclient.BuildLoanWithClientUseCase;
import pe.com.ask.usecase.updateloanapplication.getclientandloantype.GetClientAndLoanTypeUseCase;
import pe.com.ask.usecase.updateloanapplication.getstatus.GetStatusUseCase;
import pe.com.ask.usecase.updateloanapplication.updateloan.UpdateLoanUseCase;

@Configuration
@ComponentScan(basePackages = "pe.com.ask.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    // CreateLoanApplicationUseCase

    @Bean
    ValidateLoanTypeUseCase  validateLoanTypeUseCase(
            LoanTypeRepository loanTypeRepository,
            CustomLogger logger
    ){
        return new  ValidateLoanTypeUseCase(loanTypeRepository, logger);
    }

    @Bean
    ValidateLoanAmountUseCase  validateLoanAmountUseCase(CustomLogger logger){
        return new ValidateLoanAmountUseCase(logger);
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


    // GetAllLoanApplicationUnderReviewUseCase

    @Bean
    GetStatusIdsUseCase  getStatusIdsUseCase(
            StatusRepository statusRepository,
            CustomLogger logger
    ){
        return new  GetStatusIdsUseCase(
                statusRepository,
                logger
        );
    }

    @Bean
    GetLoansAndTotalUseCase getLoansAndTotalUseCase(
            LoanApplicationRepository loanApplicationRepository,
            CustomLogger logger
    ){
        return new  GetLoansAndTotalUseCase(
                loanApplicationRepository,
                logger
        );
    }

    @Bean
    GetAllStatusProcessUseCase  getAllStatusProcessUseCase(
            StatusRepository statusRepository,
            CustomLogger logger
    ){
        return new GetAllStatusProcessUseCase(
                statusRepository,
                logger
        );
    }

    @Bean
    GetClientsByIdsUseCase  getClientsByIdsUseCase(
            ClientSnapshotRepository clientSnapshotRepository,
            CustomLogger logger
    ){
        return new GetClientsByIdsUseCase(
                clientSnapshotRepository,
                logger
        );
    }

    @Bean
    GetAllLoanTypesUseCase  getAllLoanTypesUseCase(
            LoanTypeRepository loanTypeRepository,
            CustomLogger logger
    ){
        return new GetAllLoanTypesUseCase(
                loanTypeRepository,
                logger
        );
    }

    @Bean
    LoanProcessUseCase  loanProcessUseCase(
            CustomLogger logger
    ){
        return new LoanProcessUseCase(
                logger
        );
    }

    @Bean
    GetAllLoanApplicationUnderReviewUseCase getAllLoanApplicationUnderReviewUseCase(
            GetStatusIdsUseCase getStatusIdsUseCase,
            GetLoansAndTotalUseCase getLoansAndTotalUseCase,
            GetAllStatusProcessUseCase getAllStatusProcessUseCase,
            GetClientsByIdsUseCase getClientsByIdsUseCase,
            GetAllLoanTypesUseCase getAllLoanTypesUseCase,
            LoanProcessUseCase loanProcessUseCase,
            CustomLogger logger
    ) {
        return new  GetAllLoanApplicationUnderReviewUseCase(
                getStatusIdsUseCase,
                getLoansAndTotalUseCase,
                getAllStatusProcessUseCase,
                getClientsByIdsUseCase,
                getAllLoanTypesUseCase,
                loanProcessUseCase,
                logger
        );
    }

    // UpdateLoanApplicationUseCase

    @Bean
    GetStatusUseCase  getStatusUseCase(
            StatusRepository statusRepository,
            CustomLogger logger
    ){
        return new  GetStatusUseCase(statusRepository, logger);
    }

    @Bean
    UpdateLoanUseCase updateLoanUseCase(
            LoanApplicationRepository loanApplicationRepository,
            CustomLogger logger
    ){
        return new UpdateLoanUseCase(loanApplicationRepository, logger);
    }

    @Bean
    GetClientAndLoanTypeUseCase  getClientAndLoanTypeUseCase(
            ClientSnapshotRepository clientSnapshotRepository,
            LoanTypeRepository loanTypeRepository,
            CustomLogger logger
    ){
        return new GetClientAndLoanTypeUseCase(clientSnapshotRepository, loanTypeRepository, logger);
    }

    @Bean
    BuildLoanWithClientUseCase buildLoanWithClientUseCase(){
        return new BuildLoanWithClientUseCase();
    }

    @Bean
    UpdateLoanApplicationUseCase updateLoanApplicationUseCase(
            GetStatusUseCase getStatusUseCase,
            UpdateLoanUseCase  updateLoanUseCase,
            GetClientAndLoanTypeUseCase getClientAndLoanTypeUseCase,
            BuildLoanWithClientUseCase buildLoanWithClientUseCase,
            LoanApplicationSQSGateway loanApplicationSQSGateway,
            CustomLogger logger
    ){
        return new UpdateLoanApplicationUseCase(
                getStatusUseCase,
                updateLoanUseCase,
                getClientAndLoanTypeUseCase,
                buildLoanWithClientUseCase,
                loanApplicationSQSGateway,
                logger);
    }
}