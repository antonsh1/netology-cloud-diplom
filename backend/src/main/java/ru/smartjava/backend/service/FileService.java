package ru.smartjava.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.entity.FileItem;

import java.io.File;
import java.util.List;

public interface FileService {

    List<FileItem> getFileList(Integer numberFiles);
    void deleteFile(String fileName);

    Resource loadAsResource(String fileName);

    void storeFile(MultipartFile file);

    void renameFile(String sourceFileName, String destinationFileName);

}
