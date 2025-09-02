package pe.com.ask.api.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserIdentityAdapterTest {

    private UserIdentityAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UserIdentityAdapter();
    }

    @Test
    void testGetCurrentUserId_withJwt() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("userId")).thenReturn("12345");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(jwt);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        try (MockedStatic<ReactiveSecurityContextHolder> mockedHolder = Mockito.mockStatic(ReactiveSecurityContextHolder.class)) {
            mockedHolder.when(ReactiveSecurityContextHolder::getContext).thenReturn(Mono.just(context));

            StepVerifier.create(adapter.getCurrentUserId())
                    .expectNext("12345")
                    .verifyComplete();
        }
    }

    @Test
    void testGetCurrentUserId_noJwt() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("principalNotJwt");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        try (MockedStatic<ReactiveSecurityContextHolder> mockedHolder = Mockito.mockStatic(ReactiveSecurityContextHolder.class)) {
            mockedHolder.when(ReactiveSecurityContextHolder::getContext).thenReturn(Mono.just(context));

            StepVerifier.create(adapter.getCurrentUserId())
                    .verifyComplete();
        }
    }

    @Test
    void testGetCurrentUserDni_withJwt() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("userDni")).thenReturn("98765432");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(jwt);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        try (MockedStatic<ReactiveSecurityContextHolder> mockedHolder = Mockito.mockStatic(ReactiveSecurityContextHolder.class)) {
            mockedHolder.when(ReactiveSecurityContextHolder::getContext).thenReturn(Mono.just(context));

            StepVerifier.create(adapter.getCurrentUserDni())
                    .expectNext("98765432")
                    .verifyComplete();
        }
    }

    @Test
    void testGetCurrentUserDni_noJwt() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("someOtherPrincipal");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        try (MockedStatic<ReactiveSecurityContextHolder> mockedHolder = Mockito.mockStatic(ReactiveSecurityContextHolder.class)) {
            mockedHolder.when(ReactiveSecurityContextHolder::getContext).thenReturn(Mono.just(context));

            StepVerifier.create(adapter.getCurrentUserDni())
                    .verifyComplete();
        }
    }

}