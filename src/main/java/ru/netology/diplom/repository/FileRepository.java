package ru.netology.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.diplom.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, String> {

    FileEntity findByFilename(String filename);
    FileEntity findById(Long id);

}