package ru.netology.diplom.service;

import ru.netology.diplom.dto.Credentials;

import java.util.Map;

public interface AuthorizationService {
    Map<String,String> login(Credentials loginPass);

    void checkToken(String token);

    void logout(String token);
}