package pe.com.ask.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.handlers.GetAllLoanApplicationUnderReviewHandler;
import pe.com.ask.api.utils.routes.Routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class GetAllLoanApplicationUnderReviewRouterRest {

    @Bean
    public RouterFunction<ServerResponse> getAllLoanApplicationUnderReviewRouterFunction(
            GetAllLoanApplicationUnderReviewHandler getAllLoanApplicationUnderReviewHandler
    ) {
        return route(GET(Routes.GET_LOANS_APPLICATIONS), getAllLoanApplicationUnderReviewHandler::listenGETAllLoanApplicationUnderReviewUseCase);
    }
}