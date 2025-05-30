package org.nikolait.assigment.telegramwebappauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TelegramWebappAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramWebappAuthApplication.class, args);
    }

}
