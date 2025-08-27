package pe.com.ask.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMapperConfigTest {

    @Test
    @DisplayName("Should provide ObjectMapper bean as an instance of ObjectMapperImp")
    void testObjectMapperBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ObjectMapperConfig.class);
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        assertNotNull(objectMapper);
        assertInstanceOf(ObjectMapperImp.class, objectMapper);
        context.close();
    }
}