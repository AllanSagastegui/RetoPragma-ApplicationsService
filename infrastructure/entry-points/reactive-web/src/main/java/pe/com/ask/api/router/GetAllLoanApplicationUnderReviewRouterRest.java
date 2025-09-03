package pe.com.ask.api.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.doc.GetAllLoanApplicationUnderReviewDoc;
import pe.com.ask.api.handlers.GetAllLoanApplicationUnderReviewHandler;
import pe.com.ask.api.utils.routes.Routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class GetAllLoanApplicationUnderReviewRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = Routes.GET_LOANS_APPLICATIONS,
                    method = RequestMethod.GET,
                    beanClass = GetAllLoanApplicationUnderReviewDoc.class,
                    beanMethod = "getAllLoanApplicationsUnderReviewDoc"
            )
    })
    public RouterFunction<ServerResponse> getAllLoanApplicationUnderReviewRouterFunction(
            GetAllLoanApplicationUnderReviewHandler getAllLoanApplicationUnderReviewHandler
    ) {
        return route(GET(Routes.GET_LOANS_APPLICATIONS), getAllLoanApplicationUnderReviewHandler::listenGETAllLoanApplicationUnderReviewUseCase);
    }
}