package ru.netology.diplom.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.netology.diplom.AbstractIntegrationTest;
import ru.netology.diplom.entity.UserEntity;

import java.util.Optional;


@DisplayName("Интеграционное тестирование методов репозитория для работы с пользователями.")
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;


    private static final String TEST_USERNAME ="testtest@mail.ru";
    private static final String TEST_PASSWORD ="password123";
    private static final String TEST_TOKEN ="sampleToken";

    @Test
    @DataSet("datasets/users.yml")
    @DisplayName("Поиск пользователя по логину и паролю")
    public void testFindByEmailAndPassword() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(TEST_USERNAME);
        userEntity.setPassword(TEST_PASSWORD);

        Optional<UserEntity> foundUser = userRepository.findByEmailAndPassword(TEST_USERNAME, TEST_PASSWORD);
        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().getEmail()).isEqualTo(TEST_USERNAME);
    }

    @Test
    @DataSet("datasets/users.yml")
    @DisplayName("Поиск пользователя по токену")
    public void testFindByToken() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(TEST_USERNAME);
        userEntity.setPassword(TEST_PASSWORD);
        userEntity.setToken(TEST_TOKEN);

        Optional<UserEntity> foundUser = userRepository.findByToken(TEST_TOKEN);
        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().getToken()).isEqualTo(TEST_TOKEN);
    }

    @Test
    @DataSet("datasets/users.yml")
    @DisplayName("Поиск пользователя по неверным кредам")
    public void testFindByEmailAndPassword_NotFound() {
        Optional<UserEntity> foundUser = userRepository.findByEmailAndPassword("nonexistent@example.com", "wrongPassword");
        Assertions.assertThat(foundUser).isNotPresent();
    }

    @Test
    @DataSet("datasets/users.yml")
    @DisplayName("Поиск пользователя по неверному токену")
    public void testFindByToken_NotFound() {
        Optional<UserEntity> foundUser = userRepository.findByToken("nonexistentToken");
        Assertions.assertThat(foundUser).isNotPresent();
    }
}