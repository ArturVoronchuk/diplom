package ru.netology.diplom.UnitTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.netology.diplom.dto.FileRequest;
import ru.netology.diplom.dto.FileResponse;
import ru.netology.diplom.entity.FileEntity;
import ru.netology.diplom.exceptions.FileNotFoundException;
import ru.netology.diplom.repository.FileRepository;
import ru.netology.diplom.repository.UserRepository;
import ru.netology.diplom.service.FileService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileResponse fileResponse;

    @Mock
    private UserRepository userRepository;

    private ObjectMapper objectMapper;
    private List<FileEntity> files;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        files = Arrays.asList(
                new FileEntity(),
                new FileEntity()
        );
    }

    @Test
    void uploadFile_shouldSaveFileToDatabase() throws IOException {
        String filename = "test-file.jpg";

        byte[] fileContent = new byte[1024];

        FileEntity fileEntity = new FileEntity();
        fileEntity.setData(fileContent);
        fileEntity.setFilename(filename);

        when(fileRepository.save(any(FileEntity.class))).thenReturn(fileEntity);

        fileService.uploadFile(filename, new FileRequest());

        verify(fileRepository).save(any(FileEntity.class));
    }

    @Test
    void deleteFile_shouldRemoveFileFromDatabase() {
        String filename = "test-file.jpg";
        FileEntity file = new FileEntity();
        file.setFilename(filename);

        when(fileRepository.findByFilename(filename)).thenReturn(file);
        doNothing().when(fileRepository).delete(any(FileEntity.class));

        fileService.deleteFile(filename);

        verify(fileRepository).findByFilename(filename);
        verify(fileRepository).delete(file);
    }

    @Test
    void testDeleteFile_Success() {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename("test.txt");

        when(fileRepository.findByFilename("test.txt")).thenReturn(fileEntity);

        fileService.deleteFile("test.txt");

        verify(fileRepository).delete(fileEntity);
    }

    @Test
    void testDeleteFile_EmptyFilename() {
        assertThrows(FileNotFoundException.class, () -> fileService.deleteFile(""));
    }

    @Test
    public void testRenameFile_Success() {
        String oldFilename = "oldFilename.txt";
        FileResponse newFilename = new FileResponse(0, "newFilename.txt", 0);

        FileEntity existingFile = new FileEntity();
        existingFile.setFilename(oldFilename);

        when(fileRepository.findByFilename(oldFilename)).thenReturn(existingFile);

        fileService.renameFile(oldFilename, newFilename);

        verify(fileRepository).findByFilename(oldFilename);
        verify(fileRepository).save(existingFile);

        assertEquals("newFilename.txt", existingFile.getFilename());
    }

    @Test
    public void testGetFileList() {
        FileEntity file1 = new FileEntity(1L, "file1.txt", 1234, new byte[]{});
        FileEntity file2 = new FileEntity(2L, "file2.txt", 5678, new byte[]{});
        List<FileEntity> files = Arrays.asList(file1, file2);

        Page<FileEntity> filesPage = new PageImpl<>(files);

        when(fileRepository.findAll(PageRequest.of(0, 10))).thenReturn(filesPage);

        List<FileResponse> result = fileService.getFileList(10);

        assertEquals(2, result.size());
        assertEquals("file1.txt", result.get(0).getFilename());
        assertEquals("file2.txt", result.get(1).getFilename());
    }

    @Test
    public void testGetFileListEmptyResult() {
        when(fileRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(Collections.emptyList()));

        List<FileResponse> result = fileService.getFileList(10);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetFileListThrowsException() {
        when(fileRepository.findAll(PageRequest.of(0, 10))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> fileService.getFileList(10));
    }

    @Test
    public void testGetFileListWithDifferentPageSize() {
        FileEntity file1 = new FileEntity(1L, "file1.txt", 1234, new byte[]{});
        FileEntity file2 = new FileEntity(2L, "file2.txt", 5678, new byte[]{});
        List<FileEntity> files = Arrays.asList(file1, file2);

        Page<FileEntity> filesPage = new PageImpl<>(files);

        when(fileRepository.findAll(PageRequest.of(0, 5))).thenReturn(filesPage);

        List<FileResponse> result = fileService.getFileList(5);

        assertEquals(2, result.size());
    }

    @Test
    public void testFileEntityToFileResponseConversion() {
        FileEntity file = new FileEntity(1L, "file1.txt", 1234, new byte[]{});

        Page<FileEntity> filesPage = new PageImpl<>(Collections.singletonList(file));
        when(fileRepository.findAll(PageRequest.of(0, 10))).thenReturn(filesPage);

        List<FileResponse> result = fileService.getFileList(10);

        assertEquals("file1.txt", result.get(0).getFilename());
        assertEquals(1234, result.get(0).getSize());
    }
}