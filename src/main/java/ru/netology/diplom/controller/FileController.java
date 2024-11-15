package ru.netology.diplom.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplom.dto.FileRequest;
import ru.netology.diplom.dto.FileResponse;
import ru.netology.diplom.entity.FileEntity;
import ru.netology.diplom.repository.FileRepository;
import ru.netology.diplom.service.AuthorizationService;
import ru.netology.diplom.service.FileService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class FileController {

    private final FileService fileService;
    private final AuthorizationService authorizedService;

    private final FileRepository fileRepository;

    @SneakyThrows
    @PostMapping(value = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void uploadFile(@RequestHeader("auth-token") String tokenHeader,
                           @RequestParam("filename") String filename,
                           @RequestPart MultipartFile file) {
        authorizedService.checkToken(tokenHeader.replace("Bearer ", ""));
        FileRequest fileRequest = FileRequest.builder().file(file.getBytes()).size(file.getSize()).build();
        fileService.uploadFile(filename, fileRequest);
    }

    @DeleteMapping("/{filename}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFile(@RequestHeader("auth-token") String tokenHeader,
                           @RequestParam("filename") String filename) {
        authorizedService.checkToken(tokenHeader.replace("Bearer ", ""));
        fileService.deleteFile(filename);
    }

    @PutMapping("/{filename}")
    @ResponseStatus(HttpStatus.OK)
    public void renameFile(@RequestHeader("auth-token") String token,
                           @RequestParam("filename") String filename,
                           @RequestBody FileResponse newFilename) {
        authorizedService.checkToken(token.replace("Bearer ", ""));
        fileService.renameFile(filename, newFilename);
    }

    @GetMapping("/{filename}")
    public FileEntity downloadFile(@RequestHeader("auth-token") String token,
                                   @RequestParam("filename") String filename) {
        return fileRepository.findByFilename(filename);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileResponse>> list(@RequestHeader("auth-token") String tokenHeader,
                                                   @RequestParam("limit") int limit) {

        authorizedService.checkToken(tokenHeader.replace("Bearer ", ""));
        List<FileResponse> fileList = fileService.getFileList(limit);
        return ResponseEntity.ok(fileList);
    }
//    Заглушка
//    @GetMapping("/list")
//    public ResponseEntity<List<FileResponse>> list(@RequestHeader("auth-token") String tokenHeader,
//                                                   @RequestParam("limit") int limit) {
//        authorizedService.checkToken(tokenHeader.replace("Bearer ", ""));
//        return ResponseEntity.ok(new ArrayList<>());
//    }
}