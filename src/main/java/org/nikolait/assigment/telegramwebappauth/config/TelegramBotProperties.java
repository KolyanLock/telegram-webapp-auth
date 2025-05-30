package org.nikolait.assigment.telegramwebappauth.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotProperties {
    @JsonIgnore
    private String apiToken;
    private int initDataValidityMinutes;
}
