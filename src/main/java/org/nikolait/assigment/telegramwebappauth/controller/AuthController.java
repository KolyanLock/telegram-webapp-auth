package org.nikolait.assigment.telegramwebappauth.controller;

import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.telegramwebappauth.service.impl.TelegramValidationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final TelegramValidationServiceImpl telegramValidationService;

    @PostMapping("/auth")
    public ResponseEntity<Void> auth(@RequestBody String initData) {
        if (telegramValidationService.validateInitData(initData)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(UNAUTHORIZED).build();
    }
}
