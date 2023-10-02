package ru.smartjava.backend.service;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.smartjava.backend.TestBackendApplication;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.exceptions.CustomBadRequestException;
import ru.smartjava.backend.repositories.FileRepository;
import ru.smartjava.backend.repositories.FileRepositoryImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestBackendApplication.class)
@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @MockBean
    FileRepositoryImpl fileRepository;
    @Autowired
    FileServiceImpl fileService;

    @Test
    void testFindFile() {
        when(fileRepository.findFile("rightFile")).thenReturn(Optional.of(new File("rightFile")));
        when(fileRepository.findFile("wrongFile")).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomBadRequestException.class, () -> fileService.deleteFile("wrongFile"));
        Assertions.assertDoesNotThrow(() -> fileService.deleteFile("rightFile"));
    }

    @Test
    void testFileList() {
        Integer limit = 3;
        List<File> fileList = new ArrayList<>(List.of(
                new File("1"),
                new File("2"),
                new File("3")
        ));
        List<FileItem> fileItems = new ArrayList<>(List.of(
                new FileItem("1",0),
                new FileItem("2",0),
                new FileItem("3",0)
        ));
        when(fileRepository.getFileList(limit)).thenReturn(fileList);

        Assertions.assertEquals(limit, fileService.getFileList(limit).size());
        Assertions.assertEquals(fileItems,fileService.getFileList(limit));
    }

    @Test
    void renameFile() {
        when(fileRepository.renameFile(new File("any"),new File("any"))).thenReturn(Boolean.FALSE);
        Assertions.assertThrows(CustomBadRequestException.class, () -> fileService.renameFile("any","any"));
//        Assertions.assertDoesNotThrow(() -> fileService.deleteFile("rightFile"));
    }

}
