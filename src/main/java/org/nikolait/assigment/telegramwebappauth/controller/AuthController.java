package org.nikolait.assigment.telegramwebappauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/auth")
    public ResponseEntity<Void> auth(@RequestBody String initData) throws InterruptedException {
        System.out.println("Received initData: " + initData);
        TimeUnit.SECONDS.sleep(10);
        return ResponseEntity.ok().build();
    }
}
