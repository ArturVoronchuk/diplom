package ru.netology.diplom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.diplom.dto.Credentials;
import ru.netology.diplom.service.AuthorizationServiceImpl;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final AuthorizationServiceImpl authorizationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credentials loginPass) {
        return ResponseEntity.ok(authorizationService.login(loginPass));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader("auth-token") String tokenHeader) {
        authorizationService.logout(tokenHeader.replace("Bearer ", ""));
    }
}