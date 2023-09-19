package ru.smartjava.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.repository.FileRepository;
import ru.smartjava.backend.repository.FileRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    private final FileRepository fileRepository;

    @Override
    public List<FileItem> getFileList(Integer numberFiles) {
        return new ArrayList<>();
    }
}
