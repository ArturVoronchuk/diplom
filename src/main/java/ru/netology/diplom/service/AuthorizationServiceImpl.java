package ru.netology.diplom.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import ru.netology.diplom.exceptions.UnauthorazedExceprion;
import ru.netology.diplom.repository.UserRepository;
import ru.netology.diplom.entity.UserEntity;
import ru.netology.diplom.dto.Credentials;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;

    public Map<String,String> login(Credentials loginPass) {
        Optional<UserEntity> userOptional = userRepository.findByEmailAndPassword(loginPass.getLogin(), loginPass.getPassword());

        if (userOptional.isEmpty()) {
            throw new UnauthorazedExceprion();
        }
        UserEntity user = userOptional.get();
        Map<String, String> map = new HashMap<>();
        String token = new String(new Base64().encode((user.getEmail() + "_" + Instant.now()).getBytes(StandardCharsets.UTF_8)));
        user.setToken(token);
        map.put("auth-token", token);
        return map;
    }

    @Override
    public void checkToken(String token) {
        Optional<UserEntity> userOptional = userRepository.findByToken(token);
        if (userOptional.isEmpty()) {
            throw new UnauthorazedExceprion();
        }
    }

    @Override
    public void logout(String token) {
        Optional<UserEntity> userOptional = userRepository.findByToken(token);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            userEntity.setToken(null);
            userRepository.save(userEntity); //сохраняем изменения
        }
    }
}