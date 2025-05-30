package org.nikolait.assigment.telegramwebappauth.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.nikolait.assigment.telegramwebappauth.dto.WebAppInitData;
import org.nikolait.assigment.telegramwebappauth.entity.User;
import org.nikolait.assigment.telegramwebappauth.service.AuthService;
import org.nikolait.assigment.telegramwebappauth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/auth")
    public ResponseEntity<Void> auth(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody WebAppInitData payload
    ) {
        System.out.println("webAppData: " + payload.getUser());
        userService.syncWithDatabase(modelMapper.map(payload.getUser(), User.class));
        authService.validateTelegramAuth(authHeader);
        return ResponseEntity.ok().header("Authorization", authHeader).build();
    }
}
