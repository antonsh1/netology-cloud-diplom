package ru.smartjava.backend.repositories;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.config.Constants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FileRepositoryImpl implements FileRepository {

    @Value("${cloud-settings.filestorepath}")
    private String fileStorePath;

    public List<File> getFileList(Integer limit) {
        return Arrays.stream(Objects.requireNonNull(new File(fileStorePath).listFiles())).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Optional<File> getFile(String fileName) {
        return Arrays.stream(Objects.requireNonNull(new File(fileStorePath).listFiles())).filter(file -> file.getName().equals(fileName)).findFirst();
    }

    @Override
    public Boolean deleteFile(String fileName) {
        return new File(fullPathFileName(fileName)).delete();
    }

    public Boolean saveFile(String filename, MultipartFile file) {
        Path destinationFile = new File(fullPathFileName(filename)).toPath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public Boolean renameFile(String sourceFileName, String destinationFileName) {
        File sourceFile = new File(fullPathFileName(sourceFileName));
        File destinationFile = new File(fullPathFileName(destinationFileName));
        return sourceFile.renameTo(destinationFile);
    }

    public Optional<Resource> getFileAsResource(String fileName) {
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(fullPathFileName(fileName)));
            return Optional.of(resource);
        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    String fullPathFileName(String fileName) {
        return fileStorePath + "/" + fileName;
    }


    @PostConstruct
    public void init() {
        if (!new File(fileStorePath).exists()) {
            if(!new File(fileStorePath).mkdirs()) {
                throw new RuntimeException(Constants.storageFolderCreationError);
            }
        }
    }
}
