package ru.netology.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.diplom.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    Optional<UserEntity> findByToken(String token);

}