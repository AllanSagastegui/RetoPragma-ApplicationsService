package pe.com.ask.api.filter;

import lombok.NonNull;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import pe.com.ask.api.exception.model.UnauthorizedException;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
public class UserContextFilter implements WebFilter {

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .flatMap(token -> {
                    String userId = token.getToken().getClaimAsString("userId");
                    if(userId == null) {
                        return Mono.error(new UnauthorizedException());
                    }
                    return chain.filter(exchange)
                            .contextWrite(Context.of("userId ", userId));
                });
    }
}