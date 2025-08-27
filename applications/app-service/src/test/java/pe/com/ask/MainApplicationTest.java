package pe.com.ask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainApplicationTest {
    @Test
    @DisplayName("Should load Spring application context without errors")
    void contextLoads() {
        // This test is intentionally left empty because
        // it only verifies that the Spring context starts successfully.
    }
}