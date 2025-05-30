package org.nikolait.assigment.telegramwebappauth.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.nikolait.assigment.telegramwebappauth.dto.TelegramUser;
import org.nikolait.assigment.telegramwebappauth.entity.User;
import org.nikolait.assigment.telegramwebappauth.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public void registerIfAbsent(
            @RequestAttribute TelegramUser telegramUser
    ) {
        userService.createOrUpdate(modelMapper.map(telegramUser, User.class));
    }

    @GetMapping("/info")
    public User getInfo(
            @RequestAttribute TelegramUser telegramUser
    ) {
        return userService.getUserById(telegramUser.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "User not found: %s".formatted(telegramUser)
                ));
    }

}
