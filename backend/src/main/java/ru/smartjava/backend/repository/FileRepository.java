package ru.smartjava.backend.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface FileRepository {

    List<File> getFileList();

    Boolean deleteFile(File file);

    Optional<File> getFile(String fileName);
    Boolean saveFile(MultipartFile file);

    Boolean renameFile(File sourceFile, File destinationFile);

}
