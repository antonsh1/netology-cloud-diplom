package ru.smartjava.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.exceptions.CustomInternalServerError;
import ru.smartjava.backend.repository.FileRepository;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    private final FileRepository fileRepository;

    @Override
    public List<FileItem> getFileList(Integer numberFiles) {
        return fileRepository.getFileList();
    }

    @Override
    public void deleteFile(String fileName) {
        fileRepository.deleteFile(fileName);
    }

    public Resource loadAsResource(String fileName) {
        Optional<File> getFile = fileRepository.getFile(fileName);
        if (getFile.isPresent()) {
            URI fileURI = getFile.get().toURI();
            Resource resource = null;
            try {
                resource = new UrlResource(fileURI);
            } catch (MalformedURLException e) {
                //
            }
            if (resource != null && resource.exists() || Objects.requireNonNull(resource).isReadable()) {
                return resource;
            }
        }
        throw new CustomInternalServerError("Файл не найден!");
    }

    public void storeFile(MultipartFile file) {
        fileRepository.saveFile(file);
    }

    public void renameFile(String source, String destination) {

    }
}
