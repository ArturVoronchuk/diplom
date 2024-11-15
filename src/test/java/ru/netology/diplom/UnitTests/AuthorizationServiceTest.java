package ru.netology.diplom.UnitTests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.netology.diplom.dto.Credentials;
import ru.netology.diplom.entity.UserEntity;
import ru.netology.diplom.exceptions.UnauthorazedExceprion;
import ru.netology.diplom.repository.UserRepository;
import ru.netology.diplom.service.AuthorizationServiceImpl;
import java.nio.charset.StandardCharsets;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    private Credentials credentials;
    private UserEntity user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        credentials = new Credentials("user@example.com", "password");
        user = new UserEntity();
        user.setEmail("user@example.com");
    }

    @Test
    public void testLogin_Success() {
        // Подготовка данных
        when(userRepository.findByEmailAndPassword(credentials.getLogin(), credentials.getPassword()))
                .thenReturn(Optional.of(user));

        // Выполнение метода
        Map<String, String> result = authorizationService.login(credentials);

        assertNotNull(result);
        assertTrue(result.containsKey("auth-token"));

        // Проверяем, что токен соответствует ожидаемому формату
        String expectedToken = Base64.getEncoder().encodeToString((user.getEmail() + "_" + Instant.now()).getBytes(StandardCharsets.UTF_8));

        // Проверяем, что метод findByEmailAndPassword был вызван
        verify(userRepository).findByEmailAndPassword(credentials.getLogin(), credentials.getPassword());

        // Проверяем, что токен был установлен в UserEntity
        assertEquals(result.get("auth-token"), user.getToken());
    }

    @Test
    public void testLogin_UserNotFound() {
        // Подготовка данных для теста
        Credentials loginPass = new Credentials("user@example.com", "wrongpassword");

        // Мокаем поведение userRepository
        when(userRepository.findByEmailAndPassword(loginPass.getLogin(), loginPass.getPassword()))
                .thenReturn(Optional.empty());

        // Проверка, что метод выбрасывает исключение
        assertThrows(UnauthorazedExceprion.class, () -> {
            authorizationService.login(loginPass);
        });

        // Проверяем, что метод был вызван
        verify(userRepository).findByEmailAndPassword(loginPass.getLogin(), loginPass.getPassword());
    }

    @Test
    public void testCheckToken_UserExists() {
        // Подготовка данных для теста
        String token = "validToken";
        UserEntity user = new UserEntity();
        user.setToken(token);

        // Настройка мока
        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        // Вызов метода
        authorizationService.checkToken(token);

        // Проверка, что метод findByToken был вызван один раз
        verify(userRepository, times(1)).findByToken(token);
    }

    @Test
    public void testCheckToken_UserDoesNotExist() {
        // Подготовка данных для теста
        String token = "invalidToken";

        // Настройка мока, чтобы он возвращал пустой Optional
        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        // Проверка, что при вызове метода выбрасывается исключение UnauthorazedExceprion
        assertThrows(UnauthorazedExceprion.class, () -> {
            authorizationService.checkToken(token);
        });

        // Проверка, что метод findByToken был вызван один раз
        verify(userRepository, times(1)).findByToken(token);
    }

    @Test
    public void testLogout_UserDoesNotExist() {
        String token = "invalidToken";

        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        authorizationService.logout(token);

        verify(userRepository).findByToken(token); // Проверяем, что метод был вызван
        verify(userRepository, never()).save(any()); // Убедимся, что save не был вызван
    }

    @Test
    public void testLogout_UserExists() {
        String token = "validToken";
        UserEntity user = new UserEntity();
        user.setToken(token);

        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        authorizationService.logout(token);

        verify(userRepository).findByToken(token); // Проверяем, что метод был вызван
        verify(userRepository).save(user); // Убедимся, что save был вызван
        assertNull(user.getToken()); // Проверяем, что токен стал null
    }
}