package pe.com.ask.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.doc.CreateLoanApplicationDoc;
import pe.com.ask.api.utils.routes.Routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoanApplicationRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    method = RequestMethod.POST,
                    beanClass = CreateLoanApplicationDoc.class,
                    beanMethod = "createLoanApplicationDoc"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(LoanApplicationHandler loanApplicationHandler) {
        return route(POST(Routes.CREATE_LOAN_APPLICATION), loanApplicationHandler::listenPOSTCreateLoanApplicationUseCase)
                .andRoute(GET(Routes.GET_LOANS_APPLICATIONS), loanApplicationHandler::listenGETAllLoanApplicationUnderReviewUseCase);
    }
}