package ru.smartjava.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${cloud-settings.filestorepath}")
    private String fileStorePath;

    private final FileRepository fileRepository;

    @Override
    public List<FileItem> getFileList(Integer numberFiles) {
        return fileRepository
                .getFileList(numberFiles)
                .stream()
                .map(file -> new FileItem(file.getName(), Math.toIntExact(file.length())))
                .toList();
    }

    @Override
    public void deleteFile(String fileName) {
        Optional<File> fileToDelete = fileRepository.findFile(fileName);
        if (fileToDelete.isPresent()) {
            if (!fileRepository.deleteFile(fileToDelete.get())) {
                throw new CustomInternalServerError("Ошибка удаления файла " + fileName);
            }
        } else {
            throw new CustomInternalServerError("Файл не найден " + fileName);
        }
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
            if (
                    resource != null &&
                            (resource.exists() || Objects.requireNonNull(resource).isReadable())
            ) {
                return resource;
            }
        }
        throw new CustomInternalServerError("Файл не найден: " + fileName);
    }

    public void storeFile(MultipartFile file) {
        if(!fileRepository.saveFile(file)) {
            throw new CustomInternalServerError("Ошибка сохранения файла " + file.getName());
        };
    }

    public void renameFile(String sourceFileName, String destinationFileName) {
        File sourceFile = new File(fileStorePath + "/" + sourceFileName);
        File destinationFile = new File(fileStorePath + "/" + destinationFileName);
        if (sourceFile.exists()) {
            if (!fileRepository.renameFile(sourceFile, destinationFile)) {
                throw new CustomInternalServerError(String.format("Ошибка переименования файла из %s в %s", sourceFileName, destinationFileName) );
            }
        } else {
            throw new CustomInternalServerError("Файл источник остутствует: " + sourceFileName);
        }
    }


}
