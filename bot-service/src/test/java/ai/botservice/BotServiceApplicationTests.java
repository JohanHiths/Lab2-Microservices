package ai.botservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootTest(properties = {
        "openrouter.api.key=dummy-test-key"
})

@EnableKafka
class BotServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
