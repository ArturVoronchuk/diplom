package ru.netology.diplom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.diplom.dto.FileRequest;
import ru.netology.diplom.dto.FileResponse;
import ru.netology.diplom.entity.FileEntity;
import ru.netology.diplom.exceptions.FileNotFoundException;
import ru.netology.diplom.repository.FileRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class FileService {
    private final FileRepository fileRepository;

    public void uploadFile(String filename, FileRequest file) {
        FileEntity fileEntity = FileEntity.builder()
                .filename(filename)
                .size(file.getSize())
                .data(file.getFile())
                .build();
        fileRepository.save(fileEntity);
    }

    public void deleteFile(String filename) {
        FileEntity file = fileRepository.findByFilename(filename);
        if(!filename.isEmpty()) {
            fileRepository.delete(file);
        } else {
            throw new FileNotFoundException();
        }
    }

    public void renameFile(String filename, FileResponse newFilename) {
        FileEntity file = fileRepository.findByFilename(filename);
        if (!filename.isEmpty()) {
            file.setFilename(newFilename.getFilename());
            fileRepository.save(file);
        } else {
            throw new FileNotFoundException();
        }
    }

    public List<FileResponse> getFileList(int limit) {
        PageRequest pageable = PageRequest.of(0, limit);

        Page<FileEntity> filesPage = fileRepository.findAll(pageable);

        return filesPage.getContent().stream()
                .map(this::mapToFileResponse)
                .collect(Collectors.toList());
    }
    private FileResponse mapToFileResponse(FileEntity fileEntity) {
        return new FileResponse(
                fileEntity.getId(),
                fileEntity.getFilename(),
                fileEntity.getSize()
        );
    }
}