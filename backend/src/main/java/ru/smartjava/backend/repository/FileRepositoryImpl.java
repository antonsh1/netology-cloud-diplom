package ru.smartjava.backend.repository;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.exceptions.CustomInternalServerError;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Repository
@ConfigurationProperties("cloud-settings")
@Setter
public class FileRepositoryImpl implements FileRepository {

    private String fileStorePath;

    public List<FileItem> getFileList() {
        if (!new File(fileStorePath).exists()) {
            if (!new File(fileStorePath).mkdirs()) {
                throw new CustomInternalServerError("Ошибка создания директорий");
            }
        }
        List<FileItem> fileItems = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(new File(fileStorePath).listFiles())).forEach(file -> fileItems.add(new FileItem(file.getName(), Math.toIntExact(file.length()))));
        return fileItems;
    }

    @Override
    public void deleteFile(String fileName) {
        Optional<File> fileToDelete = Arrays.stream(new File(fileStorePath).listFiles()).filter(file -> file.getName().equals(fileName)).findFirst();
        if (fileToDelete.isPresent()) {
            if (!fileToDelete.get().delete()) {
                throw new CustomInternalServerError("Ошибка удаления файла!");
            }
        } else {
            throw new CustomInternalServerError("Файл не найден!");
        }
    }

    @Override
    public Optional<File> getFile(String fileName) {
        return Optional.of(new File(fileStorePath + "/" + fileName));
    }

    public void saveFile(MultipartFile file) {
        Path destinationFile = new File(fileStorePath + "/" + file.getOriginalFilename()).toPath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new CustomInternalServerError("Ошибка сохранения файла: " + ex.getMessage());
        }

    }

    public void renameFile(String sourceFileName, String destinationFileName) {
        File sourceFile = new File(fileStorePath + "/" + sourceFileName);
        File destinationFile = new File(fileStorePath + "/" + destinationFileName);
        if(sourceFile.exists()) {
            if(!sourceFile.renameTo(destinationFile)) {
                throw new CustomInternalServerError("Ошибка переименования");
            }
        }
    }
}
