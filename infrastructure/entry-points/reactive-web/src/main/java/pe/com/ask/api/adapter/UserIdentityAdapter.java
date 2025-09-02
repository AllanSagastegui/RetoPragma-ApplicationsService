package pe.com.ask.api.adapter;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import pe.com.ask.model.loanwithclient.gateways.UserIdentityGateway;
import reactor.core.publisher.Mono;

@Component
public class UserIdentityAdapter implements UserIdentityGateway {
    @Override
    public Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(ctx -> {
                    Object principal = ctx.getAuthentication().getPrincipal();
                    if (principal instanceof Jwt jwt) {
                        return Mono.justOrEmpty(jwt.getClaimAsString("userId"));
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<String> getCurrentUserDni() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(ctx -> {
                    Object principal = ctx.getAuthentication().getPrincipal();
                    if (principal instanceof Jwt jwt) {
                        return Mono.justOrEmpty(jwt.getClaimAsString("userDni"));
                    }
                    return Mono.empty();
                });
    }
}