package org.nikolait.assigment.telegramwebappauth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.nikolait.assigment.telegramwebappauth.config.TelegramBotProperties;
import org.nikolait.assigment.telegramwebappauth.service.TelegramValidationService;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramValidationServiceImpl implements TelegramValidationService {

    private static final String WEB_APP_DATA = "WebAppData";
    private static final String ALGORITHM = "HmacSHA256";
    private static final String AUTH_DATE_KEY = "auth_date";

    private final TelegramBotProperties telegramBotProperties;

    @Override
    public boolean validateInitData(String telegramInitData) {
        try {
            Pair<String, String> result = parseInitData(telegramInitData);
            String hash = result.first();
            String initData = result.second();
            byte[] secretKey = new HmacUtils(ALGORITHM, WEB_APP_DATA).hmac(telegramBotProperties.getApiToken());
            String initDataHash = new HmacUtils(ALGORITHM, secretKey).hmacHex(initData);

            if (!initDataHash.equals(hash)) {
                log.warn("Invalid initData hash: expected={}, received={}", initDataHash, hash);
                return false;
            }

            return checkAuthDate(telegramInitData);
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to parse initData: {}", e.getMessage(), e);
            return false;
        }
    }

    private Pair<String, String> parseInitData(String telegramInitData) throws UnsupportedEncodingException {
        Map<String, String> initData = parseQueryString(telegramInitData);
        initData = new TreeMap<>(initData);
        String hash = initData.remove("hash");

        List<String> separatedData = initData.entrySet().stream()
                .map((v) -> v.getKey() + "=" + v.getValue())
                .collect(Collectors.toList());
        return new Pair<>(hash, String.join("\n", separatedData));
    }

    private boolean checkAuthDate(String telegramInitData) throws UnsupportedEncodingException {
        Map<String, String> dataMap = parseQueryString(telegramInitData);
        String authDateValue = dataMap.get(AUTH_DATE_KEY);
        if (authDateValue == null) {
            log.warn("Missing auth_date in initData");
            return false;
        }
        long authDate;
        try {
            authDate = Long.parseLong(authDateValue);
        } catch (NumberFormatException ex) {
            log.warn("Invalid auth_date format: {}", authDateValue);
            return false;
        }
        long now = Instant.now().getEpochSecond();
        long validitySeconds = TimeUnit.MINUTES.toSeconds(telegramBotProperties.getInitDataValidityMinutes());
        if (now - authDate > validitySeconds) {
            log.warn("InitData expired: auth_date={}, now={}, validityMinutes={}",
                    authDate, now, telegramBotProperties.getInitDataValidityMinutes());
            return false;
        }
        return true;
    }

    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> parameters = new TreeMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8) : null;
            parameters.put(key, value);
        }
        return parameters;
    }

    private record Pair<F, S>(F first, S second) {
    }
}