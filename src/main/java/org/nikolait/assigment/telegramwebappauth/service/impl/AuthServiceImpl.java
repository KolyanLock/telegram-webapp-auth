package org.nikolait.assigment.telegramwebappauth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikolait.assigment.telegramwebappauth.service.AuthService;
import org.nikolait.assigment.telegramwebappauth.service.TelegramValidationService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String PREFIX = "Telegram ";

    private final TelegramValidationService telegramValidationService;

    @Override
    public void validateTelegramAuth(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(PREFIX)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid Authorization header");
        }

        String initData = authHeader.substring(PREFIX.length());

        if (!telegramValidationService.validateInitData(initData)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid Telegram initData");
        }
    }
}
