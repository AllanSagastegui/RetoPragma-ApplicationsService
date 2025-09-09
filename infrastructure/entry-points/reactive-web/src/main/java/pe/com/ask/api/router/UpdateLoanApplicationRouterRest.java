package pe.com.ask.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.handlers.UpdateLoanApplicationHandler;
import pe.com.ask.api.utils.routes.Routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UpdateLoanApplicationRouterRest {

    @Bean
    public RouterFunction<ServerResponse>  updateLoanApplicationRouterFunction(
            UpdateLoanApplicationHandler updateLoanApplicationHandler
    ) {
        return route(PUT(Routes.UPDATE_LOAN_APPLICATION),  updateLoanApplicationHandler::listenPUTUpdateLoanApplicationUseCase);
    }
}