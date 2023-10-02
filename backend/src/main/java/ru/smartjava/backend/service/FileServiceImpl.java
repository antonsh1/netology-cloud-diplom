package ru.smartjava.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.exceptions.CustomBadRequestException;
import ru.smartjava.backend.exceptions.CustomInternalServerErrorException;
import ru.smartjava.backend.repositories.FileRepository;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFile(String fileName) {
        Optional<File> fileToDelete = fileRepository.findFile(fileName);
        if (fileToDelete.isPresent()) {
            if (!fileRepository.deleteFile(fileToDelete.get())) {
                throw new CustomInternalServerErrorException(String.format("%s: %s", Constants.fileDeleteError,fileName));
            }
        } else {
            throw new CustomBadRequestException(String.format("%s: %s", Constants.fileNotFound, fileName));
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
        throw new CustomBadRequestException(String.format("%s: %s", Constants.fileNotFound, fileName));
    }

    public void storeFile(MultipartFile file) {
        if(!fileRepository.saveFile(file)) {
            throw new CustomInternalServerErrorException(String.format("%s: %s", Constants.saveFileError, file.getName()));
        };
    }

    public void renameFile(String sourceFileName, String destinationFileName) {
        File sourceFile = new File(fileStorePath + "/" + sourceFileName);
        File destinationFile = new File(fileStorePath + "/" + destinationFileName);
        if (sourceFile.exists()) {
            if (!fileRepository.renameFile(sourceFile, destinationFile)) {
                throw new CustomInternalServerErrorException(String.format("%s из %s в %s", Constants.renameFileError, sourceFileName, destinationFileName) );
            }
        } else {
            throw new CustomBadRequestException(String.format("%s: %s", Constants.sourceFileAbsent, sourceFileName));
        }
    }


}
