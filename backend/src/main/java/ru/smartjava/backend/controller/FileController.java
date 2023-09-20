package ru.smartjava.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.exceptions.BadRequestException;
import ru.smartjava.backend.service.FileService;

@Controller
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true", allowedHeaders = {"auth-token, authorization, content-type, xsrf-token"}, maxAge = 3600)
public class FileController {

    private final FileService fileService;

    //    @RolesAllowed({"UPLOAD"})
//    @Secured({"DOWNLOAD"})
    @GetMapping("list")
    ResponseEntity<String> list(@RequestParam Integer limit) {
        return ResponseEntity.ok(fileService.getFileList(limit));
    }

    @DeleteMapping("file")
    ResponseEntity<Object> list(@RequestParam String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.ok().build();
    }

    //    @GetMapping("file")
//    @ResponseBody
//    FileSystemResource getFile(@RequestParam String filename) {
//        return new FileSystemResource(fileService.getFile(filename));
//    }
    @GetMapping("file")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) {
        Resource file = fileService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("file")
    ResponseEntity<Object> uploadFile(@RequestParam String filename, @RequestParam("file") MultipartFile file) {
        fileService.storeFile(file);
        return ResponseEntity.ok().build();
    }
}
