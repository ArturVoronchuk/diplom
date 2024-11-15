package ru.netology.diplom.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.netology.diplom.AbstractIntegrationTest;
import ru.netology.diplom.entity.FileEntity;

@DisplayName("Интеграционное тестирование методов репозитория для работы с файлами.")
public class FileRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private FileRepository fileRepository;

    private static final String FILENAME = "test.txt";
    private static final String NEW_FILENAME = "test.pdf";

    @Test
    @DataSet("datasets/files.yml")
    @DisplayName("Успешный поиск по имени.")
    void findByFilename_isOk() {
        FileEntity file = fileRepository.findByFilename(FILENAME);

        Assertions.assertThat(file)
                .isNotNull()
                .extracting(FileEntity::getFilename)
                .isNotNull()
                .isEqualTo(FILENAME);
    }


    @Test
    @DataSet("datasets/files.yml")
    @DisplayName("Успешный поиск несуществующего файла.")
    void findByFilename_isNull() {
        FileEntity file = fileRepository.findByFilename("FILENAME");

        Assertions.assertThat(file).isNull();
    }

    @Test
    @DisplayName("Успешное сохранение нового файла.")
    void saveFile_isOk() {
        FileEntity newFile = new FileEntity();
        newFile.setFilename(NEW_FILENAME);

        FileEntity savedFile = fileRepository.save(newFile);

        Assertions.assertThat(savedFile)
                .isNotNull()
                .extracting(FileEntity::getFilename)
                .isEqualTo(NEW_FILENAME);

        FileEntity foundFile = fileRepository.findByFilename(NEW_FILENAME);
        Assertions.assertThat(foundFile).isNotNull();
    }

    @Test
    @DataSet("datasets/files.yml")
    @DisplayName("Успешное удаление файла.")
    void deleteFile_isOk() {
        FileEntity file = fileRepository.findByFilename(FILENAME);
        Assertions.assertThat(file).isNotNull();

        fileRepository.delete(file);

        FileEntity deletedFile = fileRepository.findByFilename(FILENAME);
        Assertions.assertThat(deletedFile).isNull();
    }

    @Test
    @DataSet("datasets/files.yml")
    @DisplayName("Успешный поиск по ID.")
    void findById_isOk() {
        Long existingFileId = 1L;

        FileEntity file = fileRepository.findById(existingFileId);

        Assertions.assertThat(file)
                .isNotNull()
                .extracting(FileEntity::getId)
                .isEqualTo(existingFileId);
    }

    @Test
    @DisplayName("Поиск по несуществующему ID возвращает пустой результат.")
    void findById_isEmpty() {
        Long nonExistentId = 999L; // ID, который точно не существует

        FileEntity file = fileRepository.findById(nonExistentId);

        Assertions.assertThat(file).isNull();
    }
}