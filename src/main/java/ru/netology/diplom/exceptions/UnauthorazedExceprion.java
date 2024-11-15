package ru.netology.diplom.exceptions;

import lombok.Getter;

/**
 * Неавторизованный пользователь
 */
@Getter
public class UnauthorazedExceprion extends RuntimeException {
    private Integer code = 4010;

}