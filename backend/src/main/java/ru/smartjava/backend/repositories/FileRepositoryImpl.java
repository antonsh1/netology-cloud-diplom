package ru.smartjava.backend.repositories;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
//@Setter
//@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository {

    @Value("${cloud-settings.filestorepath}")
    private String fileStorePath;

    public List<File> getFileList(Integer limit) {
        return Arrays.stream(Objects.requireNonNull(new File(fileStorePath).listFiles())).limit(limit).toList();
    }

    @Override
    public Optional<File> findFile(String fileName) {
        return Arrays.stream(Objects.requireNonNull(new File(fileStorePath).listFiles())).filter(file -> file.getName().equals(fileName)).findFirst();
    }

    @Override
    public Boolean deleteFile(File file) {
        return file.delete();
    }

    @Override
    public Optional<File> getFile(String fileName) {
        return Optional.of(new File(fileStorePath + "/" + fileName));
    }

    public Boolean saveFile(MultipartFile file) {
        Path destinationFile = new File(fileStorePath + "/" + file.getOriginalFilename()).toPath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public Boolean renameFile(File sourceFile, File destinationFile) {
        return sourceFile.renameTo(destinationFile);
    }

    @PostConstruct
    public void init() {
        if (!new File(fileStorePath).exists()) {
            new File(fileStorePath).mkdirs();
        }
    }
}
