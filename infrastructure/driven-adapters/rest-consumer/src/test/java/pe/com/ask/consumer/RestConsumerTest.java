package pe.com.ask.consumer;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.ask.model.gateways.CustomLogger;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;

class RestConsumerTest {

    private static MockWebServer mockBackEnd;
    private static RestConsumer restConsumer;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockBackEnd.url("/").toString())
                .build();

        CustomLogger logger = mock(CustomLogger.class);

        restConsumer = new RestConsumer(webClient, logger);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("findClientsByIds returns expected ClientSnapshot")
    void testFindClientsByIds() {
        UUID id = UUID.randomUUID();
        String jsonBody = "[{" +
                "\"id\":\"" + id + "\"," +
                "\"name\":\"John\"," +
                "\"lastName\":\"Doe\"," +
                "\"dni\":\"12345678\"," +
                "\"email\":\"john.doe@example.com\"," +
                "\"baseSalary\":5000" +
                "}]";

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200)
                .setBody(jsonBody));

        Jwt jwt = Jwt.withTokenValue("fake-token")
                .header("alg", "none")
                .claim("userId", id.toString())
                .build();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", jwt, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        StepVerifier.create(
                        restConsumer.findClientsByIds(List.of(id))
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
                )
                .expectNextMatches(client ->
                        client.getName().equals("John") &&
                                client.getId().equals(id) &&
                                client.getEmail().equals("john.doe@example.com") &&
                                client.getBaseSalary().equals(BigDecimal.valueOf(5000))
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("findClientsByIds returns expected ClientSnapshot with Jwt credentials")
    void testFindClientsByIdsWithJwt() {
        UUID id = UUID.randomUUID();
        String jsonBody = "[{" +
                "\"id\":\"" + id + "\"," +
                "\"name\":\"John\"," +
                "\"lastName\":\"Doe\"," +
                "\"dni\":\"12345678\"," +
                "\"email\":\"john.doe@example.com\"," +
                "\"baseSalary\":5000" +
                "}]";

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200)
                .setBody(jsonBody));

        Jwt jwt = Jwt.withTokenValue("fake-token")
                .header("alg", "none")
                .claim("userId", id.toString())
                .build();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", jwt, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        StepVerifier.create(
                        restConsumer.findClientsByIds(List.of(id))
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
                )
                .expectNextMatches(client ->
                        client.getName().equals("John") &&
                                client.getId().equals(id) &&
                                client.getEmail().equals("john.doe@example.com") &&
                                client.getBaseSalary().equals(BigDecimal.valueOf(5000))
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("findClientsByIds returns expected ClientSnapshot with non-Jwt credentials")
    void testFindClientsByIdsWithStringCredentials() {
        UUID id = UUID.randomUUID();
        String jsonBody = "[{" +
                "\"id\":\"" + id + "\"," +
                "\"name\":\"Jane\"," +
                "\"lastName\":\"Doe\"," +
                "\"dni\":\"87654321\"," +
                "\"email\":\"jane.doe@example.com\"," +
                "\"baseSalary\":6000" +
                "}]";

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200)
                .setBody(jsonBody));

        String tokenString = "plain-token";
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", tokenString, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        StepVerifier.create(
                        restConsumer.findClientsByIds(List.of(id))
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
                )
                .expectNextMatches(client ->
                        client.getName().equals("Jane") &&
                                client.getId().equals(id) &&
                                client.getEmail().equals("jane.doe@example.com") &&
                                client.getBaseSalary().equals(BigDecimal.valueOf(6000))
                )
                .verifyComplete();
    }
}