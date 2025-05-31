package org.nikolait.assigment.telegramwebappauth;

import org.junit.jupiter.api.Test;
import org.nikolait.assigment.telegramwebappauth.config.TestcontainersConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class TelegramWebappAuthApplicationTests {

    @Test
    void contextLoads() {
    }

}
