package ai.botservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(excludeName = {
        "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration"
})

public class BotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotServiceApplication.class, args);
    }

}
