package org.nikolait.assigment.telegramwebappauth.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacUtils;
import org.nikolait.assigment.telegramwebappauth.config.TelegramBotProperties;
import org.nikolait.assigment.telegramwebappauth.dto.TelegramUser;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
public class TelegramInitDataGenerator {

    private final ObjectMapper objectMapper;
    private final TelegramBotProperties telegramBotProperties;

    public String generateValidInitData(TelegramUser telegramUser) throws Exception {
        // 1. Формируем значения
        String userJson = objectMapper.writeValueAsString(telegramUser); // НЕ кодируем для data-check-string
        long authDate = System.currentTimeMillis() / 1000;
        String chatInstance = "-8472941990794157579";
        String chatType = "private";

        // 2. Сортированная мапа для data-check-string
        TreeMap<String, String> data = new TreeMap<>();
        data.put("auth_date", String.valueOf(authDate));
        data.put("chat_instance", chatInstance);
        data.put("chat_type", chatType);
        data.put("user", userJson);

        // 3. Собираем data-check-string (без кодирования!)
        String dataCheckString = data.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");

        // 4. Секретный ключ и hash
        byte[] secretKey = new HmacUtils("HmacSHA256", "WebAppData").hmac(telegramBotProperties.getApiToken());
        String hash = new HmacUtils("HmacSHA256", secretKey).hmacHex(dataCheckString);

        // 5. Формируем query string (тут значения кодируются!)
        StringBuilder sb = new StringBuilder();
        for (var entry : data.entrySet()) {
            if (!sb.isEmpty()) sb.append("&");
            sb.append(entry.getKey()).append("=")
              .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        sb.append("&hash=").append(hash);
        return sb.toString();
    }
} 