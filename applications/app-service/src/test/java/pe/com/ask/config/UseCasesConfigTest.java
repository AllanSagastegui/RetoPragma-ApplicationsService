package pe.com.ask.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.loanapplication.gateways.LoanApplicationRepository;
import pe.com.ask.model.loantype.gateways.LoanTypeRepository;
import pe.com.ask.model.status.gateways.StatusRepository;
import pe.com.ask.usecase.createloanapplication.CreateLoanApplicationUseCase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UseCasesConfigTest {

    @Test
    @DisplayName("Should register SignUpUseCase bean in application context")
    void testCreateLoanApplicationUseCaseBeanExist(){
        try(AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(TestConfig.class)) {
            CreateLoanApplicationUseCase createLoanApplicationUseCase = context.getBean(CreateLoanApplicationUseCase.class);
            assertNotNull(createLoanApplicationUseCase, "CreateLoanApplicationUseCase bean should be registered");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean LoanApplicationRepository loanApplicationRepository(){return mock(LoanApplicationRepository.class);}
        @Bean LoanTypeRepository loanTypeRepository(){return mock(LoanTypeRepository.class);}
        @Bean StatusRepository statusRepository(){return mock(StatusRepository.class);}
        @Bean TransactionalGateway transactionalGateway(){return mock(TransactionalGateway.class);}
        @Bean CustomLogger customLogger(){return mock(CustomLogger.class);}
    }
}