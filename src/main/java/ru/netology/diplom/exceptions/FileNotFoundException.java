package ru.netology.diplom.exceptions;

/**
 * Ошибка скачивания. Файл не существует.
 */
public class FileNotFoundException extends RuntimeException {
    private Integer code = 5020;
}
