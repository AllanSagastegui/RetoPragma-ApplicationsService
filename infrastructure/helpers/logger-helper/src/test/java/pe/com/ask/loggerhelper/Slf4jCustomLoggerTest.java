package pe.com.ask.loggerhelper;

import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Slf4jCustomLoggerTest {

    private Slf4JCustomLogger customLogger;
    private TestLogger testLogger;

    @BeforeEach
    void setUp() {
        customLogger = new Slf4JCustomLogger();
        testLogger = TestLoggerFactory.getTestLogger(Slf4JCustomLogger.class);
        TestLoggerFactory.clear();
    }

    @Test
    void testTrace() {
        customLogger.trace("trace message {}", 123);
        assertThat(testLogger.getLoggingEvents())
                .anyMatch(event -> event.getLevel().toString().equals("TRACE")
                        && event.getMessage().equals("trace message {}")
                        && event.getArguments().contains(123));
    }

    @Test
    void testInfo() {
        customLogger.info("info message {}", "arg");
        assertThat(testLogger.getLoggingEvents())
                .anyMatch(event -> event.getLevel().toString().equals("INFO")
                        && event.getMessage().equals("info message {}")
                        && event.getArguments().contains("arg"));
    }

    @Test
    void testWarn() {
        customLogger.warn("warn message {}", true);
        assertThat(testLogger.getLoggingEvents())
                .anyMatch(event -> event.getLevel().toString().equals("WARN")
                        && event.getMessage().equals("warn message {}")
                        && event.getArguments().contains(true));
    }

    @Test
    void testError() {
        customLogger.error("error message {}", "error");
        assertThat(testLogger.getLoggingEvents())
                .anyMatch(event -> event.getLevel().toString().equals("ERROR")
                        && event.getMessage().equals("error message {}")
                        && event.getArguments().contains("error"));
    }
}
