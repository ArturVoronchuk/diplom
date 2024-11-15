package ru.netology.diplom.exceptions;

import lombok.Getter;

/**
 * Ошибка удаления. Файл не существует.
 */
@Getter
public class FileAlreadyDeletedException extends RuntimeException {
    private Integer code = 5010;
}
