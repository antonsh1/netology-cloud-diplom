package ru.smartjava.backend.repository;

import org.springframework.stereotype.Repository;
import ru.smartjava.backend.entity.FileItem;

import java.util.List;
import java.util.Optional;

@Repository
public class FileRepositoryImpl implements FileRepository {

    String fileStoragePath = "backend/src/main/resources";

    Optional<List<FileItem>> getFileList() {
        return Optional.empty();
    }

}
