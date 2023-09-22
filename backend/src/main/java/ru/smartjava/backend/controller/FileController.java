package ru.smartjava.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.entity.FileToRename;
import ru.smartjava.backend.service.FileService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true", allowedHeaders = {"auth-token, authorization, content-type, xsrf-token"}, maxAge = 3600)
public class FileController {

    private final FileService fileService;

    //    @RolesAllowed({"UPLOAD"})
//    @Secured({"DOWNLOAD"})
    @GetMapping(value = "list")
    ResponseEntity<List<FileItem>> list(@NotNull @RequestParam Integer limit) {
        return ResponseEntity.ok(fileService.getFileList(limit));
    }

    @DeleteMapping("file")
    ResponseEntity<Object> deleteFile(@NotNull @RequestParam String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.ok().build();
    }

    @GetMapping("file")
    public ResponseEntity<Resource> downloadFile(@NotNull @RequestParam String filename) {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + StandardCharsets.UTF_8.encode(filename) + "\"")
                .body(fileService.loadAsResource(filename));
    }

    @PostMapping("file")
    ResponseEntity<Object> uploadFile(@NotNull @RequestParam String filename, @NotNull @RequestParam("file") MultipartFile file) {
        fileService.storeFile(file);
        return ResponseEntity.ok().build();
    }

    @PutMapping("file")
    ResponseEntity<Object> renameFile(@NotNull @RequestParam String filename, @NotNull @Valid @RequestBody FileToRename fileToRename) {
        fileService.renameFile(filename, fileToRename.getFilename());
        return ResponseEntity.ok().build();
    }
}
