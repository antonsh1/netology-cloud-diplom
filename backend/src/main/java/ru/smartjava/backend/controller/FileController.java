package ru.smartjava.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.entity.FileToRename;
import ru.smartjava.backend.service.FileService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping(value = Constants.urlListPath)
    ResponseEntity<List<FileItem>> list(@NotNull @RequestParam Integer limit) {
        return ResponseEntity.ok(fileService.getFileList(limit));
    }

    @Secured({"DELETE"})
    @DeleteMapping(Constants.urlFilePath)
    ResponseEntity<Object> deleteFile(@NotNull @RequestParam String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.ok().build();
    }

    @Secured({"DOWNLOAD"})
    @GetMapping(value = Constants.urlFilePath, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(@NotNull @RequestParam String filename) {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + StandardCharsets.UTF_8.encode(filename) + "\"")
                .body(fileService.downloadAsResource(filename));
    }

    @Secured({"UPLOAD"})
    @PostMapping(Constants.urlFilePath)
    ResponseEntity<Object> uploadFile(@NotNull @RequestParam String filename, @NotNull @RequestParam("file") MultipartFile file) {
        fileService.saveFile(filename, file);
        return ResponseEntity.ok().build();
    }

    @Secured({"RENAME"})
    @PutMapping(Constants.urlFilePath)
    ResponseEntity<Object> renameFile(@NotNull @RequestParam String filename, @NotNull @Valid @RequestBody FileToRename fileToRename) {
        fileService.renameFile(filename, fileToRename.getFilename());
        return ResponseEntity.ok().build();
    }
}
