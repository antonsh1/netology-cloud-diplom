package ru.smartjava.backend.repositories;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface FileRepository {

    List<File> getFileList(Integer limit);
    Optional<File> getFile(String fileName);
    Optional<Resource> getFileAsResource(String fileName);
    Boolean deleteFile(String fileName);
    Boolean saveFile(String fileName,   MultipartFile file);
    Boolean renameFile(String sourceFileName, String destinationFileName);

}
