package ru.smartjava.backend.service;

import ru.smartjava.backend.entity.FileItem;

import java.util.List;

public interface FileService {

    List<FileItem> getFileList(Integer numberFiles);

}
