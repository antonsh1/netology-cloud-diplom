package ru.smartjava.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.exceptions.CustomBadRequestException;
import ru.smartjava.backend.exceptions.CustomInternalServerErrorException;
import ru.smartjava.backend.repositories.FileRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

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
        if (fileRepository.getFile(fileName).isPresent()) {
            if (!fileRepository.deleteFile(fileName)) {
                throw new CustomInternalServerErrorException(String.format("%s: %s", Constants.fileDeleteError, fileName));
            }
        } else {
            throw new CustomBadRequestException(String.format("%s: %s", Constants.fileNotFound, fileName));
        }
    }

    public Resource downloadAsResource(String fileName) {
        Optional<File> optionalFile = fileRepository.getFile(fileName);
        if (optionalFile.isPresent()) {
            return fileRepository
                    .getFileAsResource(fileName)
                    .orElseThrow(() -> new CustomInternalServerErrorException(String.format("%s: %s", Constants.sourceFileReadError, fileName)));
        }
        throw new CustomBadRequestException(String.format("%s: %s", Constants.fileNotFound, fileName));
    }


    public void saveFile(String fileName, MultipartFile file) {
        if (!fileRepository.saveFile(fileName, file)) {
            throw new CustomInternalServerErrorException(String.format("%s: %s", Constants.saveFileError, file.getName()));
        }
    }

    public void renameFile(String sourceFileName, String destinationFileName) {
        if (fileRepository.getFile(sourceFileName).isPresent()) {
            if (!fileRepository.renameFile(sourceFileName, destinationFileName)) {
                throw new CustomInternalServerErrorException(String.format("%s из %s в %s", Constants.renameFileError, sourceFileName, destinationFileName));
            }
        } else {
            throw new CustomBadRequestException(String.format("%s: %s", Constants.sourceFileAbsent, sourceFileName));
        }
    }


}
