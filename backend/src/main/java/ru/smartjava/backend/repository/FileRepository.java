package ru.smartjava.backend.repository;

import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.entity.FileItem;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface FileRepository {

    List<FileItem> getFileList();
    void deleteFile(String fileName);
    Optional<File> getFile(String fileName);
    void saveFile(MultipartFile file);

}
