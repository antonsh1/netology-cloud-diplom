package ru.smartjava.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.service.FileService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true", allowedHeaders = "auth-token", maxAge = 3600)
public class FileController {

    private final FileService fileService;

    //    @RolesAllowed({"UPLOAD"})
//    @Secured({"DOWNLOAD"})
    @GetMapping("list")
    ResponseEntity<List<FileItem>> list(@RequestParam Integer numberFiles) {
        return ResponseEntity.of(Optional.ofNullable(fileService.getFileList(numberFiles)));
    }


}
