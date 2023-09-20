package ru.smartjava.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileService {

    String getFileList(Integer numberFiles);
    void deleteFile(String fileName);
    File getFile(String fileName);

    Resource loadAsResource(String fileName);

    void storeFile(MultipartFile file);

}
