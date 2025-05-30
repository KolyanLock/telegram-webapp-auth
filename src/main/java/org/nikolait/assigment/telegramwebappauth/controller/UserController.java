package org.nikolait.assigment.telegramwebappauth.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.nikolait.assigment.telegramwebappauth.dto.TelegramUser;
import org.nikolait.assigment.telegramwebappauth.entity.User;
import org.nikolait.assigment.telegramwebappauth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<Void> registerIfAbsent(
            @RequestAttribute TelegramUser telegramUser
    ) {
        System.out.println("telegramUser: " + telegramUser);
        userService.createOrUpdate(modelMapper.map(telegramUser, User.class));
        return ResponseEntity.ok().build();
    }

}
