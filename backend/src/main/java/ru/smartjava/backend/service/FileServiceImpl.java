package ru.smartjava.backend.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.exceptions.StorageException;
import ru.smartjava.backend.exceptions.StorageFileNotFoundException;
import ru.smartjava.backend.repository.FileRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    private final FileRepository fileRepository;
    private final Gson gson = new Gson();

    @Override
    public String getFileList(Integer numberFiles) {

        return gson.toJson(fileRepository.getFileList());
    }

    @Override
    public void deleteFile(String fileName) {
        fileRepository.deleteFile(fileName);
    }

    @Override
    public File getFile(String fileName) {
        Optional<File> getFile = fileRepository.getFile(fileName);
        if (getFile.isPresent()) {
            return getFile.get();
        }
        return getFile.get();
    }

    public Resource loadAsResource(String fileName) {
        Optional<File> getFile = fileRepository.getFile(fileName);
        if (getFile.isPresent()) {
            Path file = getFile.get().toPath();
            Resource resource = null;
            try {
                resource = new UrlResource(file.toUri());
            } catch (MalformedURLException e) {
                throw new StorageFileNotFoundException("Could not read file: " + fileName);
            }
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        }
            throw new StorageFileNotFoundException("Could not read file: " + fileName);
    }


    public void storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }
        fileRepository.storeFile(file);
    }
}
