package ru.smartjava.backend.repository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.exceptions.StorageException;

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
//    = "backend/src/main/resources";

    public List<FileItem> getFileList() {
        if(!new File(fileStorePath).exists()) {
            new File(fileStorePath).mkdirs();
        }
        List<FileItem> fileItems = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(new File(fileStorePath).listFiles())).forEach(file -> fileItems.add(new FileItem(file.getName(), Math.toIntExact(file.length()))));
        System.out.println(fileItems);
        //        System.out.println(Arrays.stream(new File(fileStorePath).listFiles());
//        return Optional.empty();
        return fileItems;
    }

    @Override
    public void deleteFile(String fileName) {
        Optional<File> fileToDelete = Arrays.stream(new File(fileStorePath).listFiles()).filter(file -> file.getName().equals(fileName)).findFirst();
        System.out.println("Запрос на удаление " + fileName);
        System.out.println("Удаляем " + fileToDelete.get());
        if(fileToDelete.isPresent()) fileToDelete.get().delete();
    }

    @Override
    public Optional<File> getFile(String fileName) {
        File getFile = new File(fileStorePath + "/" + fileName);
        if(getFile.exists()) {
            return Optional.of(getFile);
        } else {
            return Optional.empty();
        }
    }

    public void storeFile(MultipartFile file) {
        Path destinationFile = new File(fileStorePath + "/" + file.getOriginalFilename()).toPath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }

    }
}
