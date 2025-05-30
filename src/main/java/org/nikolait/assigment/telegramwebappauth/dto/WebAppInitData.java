package org.nikolait.assigment.telegramwebappauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebAppInitData {

    private TelegramUser user;

    @JsonProperty("chat_instance")
    private String chatInstance;

    @JsonProperty("chat_type")
    private String chatType;

    @JsonProperty("auth_date")
    private String authDate;

    private String signature;

    private String hash;

}
