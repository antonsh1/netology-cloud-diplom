package ru.smartjava.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.smartjava.backend.TestBackendApplication;
import ru.smartjava.backend.exceptions.CustomBadRequestException;
import ru.smartjava.backend.exceptions.CustomInternalServerErrorException;
import ru.smartjava.backend.repositories.FileRepositoryImpl;
import ru.smartjava.backend.utils.TestUtils;

import java.io.File;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestBackendApplication.class)
@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @MockBean
    FileRepositoryImpl fileRepository;
    @Autowired
    FileServiceImpl fileService;
    @Autowired
    private TestUtils testUtils;


    @BeforeEach
    void initFileRepository() {
        when(fileRepository.getFile(testUtils.rightFileName)).thenReturn(Optional.of(new File(testUtils.rightFileName)));
        when(fileRepository.getFile(testUtils.wrongFileName)).thenReturn(Optional.empty());
        when(fileRepository.saveFile(testUtils.rightFileName, testUtils.rightMultipartFile)).thenReturn(Boolean.TRUE);
        when(fileRepository.saveFile(testUtils.wrongFileName, testUtils.wrongMultipartFile)).thenReturn(Boolean.FALSE);
        when(fileRepository.getFileAsResource(testUtils.rightFileName)).thenReturn(Optional.of(testUtils.emptyIsr));
        when(fileRepository.getFileAsResource(testUtils.wrongFileName)).thenReturn(Optional.empty());
    }

    @Test
    void testDeleteFile() {
        when(fileRepository.deleteFile(testUtils.rightFileName)).thenReturn(Boolean.FALSE);
        Assertions.assertThrows(CustomInternalServerErrorException.class, () -> fileService.deleteFile(testUtils.rightFileName));
        when(fileRepository.deleteFile(testUtils.rightFileName)).thenReturn(Boolean.TRUE);
        Assertions.assertThrows(CustomBadRequestException.class, () -> fileService.deleteFile(testUtils.wrongFileName));
        Assertions.assertDoesNotThrow(() -> fileService.deleteFile(testUtils.rightFileName));
    }

    @Test
    void testFileList() {
        when(fileRepository.getFileList(testUtils.fileListLimit)).thenReturn(testUtils.fileList);
        Assertions.assertEquals(testUtils.fileListLimit, fileService.getFileList(testUtils.fileListLimit).size());
        Assertions.assertEquals(testUtils.fileItems, fileService.getFileList(testUtils.fileListLimit));
    }

    @Test
    void testRenameFile() {
        Assertions.assertThrows(CustomBadRequestException.class, () -> fileService.renameFile("any", "any"));
        when(fileRepository.renameFile(testUtils.rightFileName, "any")).thenReturn(Boolean.FALSE);
        Assertions.assertThrows(CustomInternalServerErrorException.class, () -> fileService.renameFile(testUtils.rightFileName, "any"));
        when(fileRepository.renameFile(testUtils.rightFileName, "any")).thenReturn(Boolean.TRUE);
        Assertions.assertDoesNotThrow(() -> fileService.renameFile(testUtils.rightFileName, "any"));
    }

    @Test
    void testSaveFile() {
        Assertions.assertDoesNotThrow(() -> fileService.saveFile(testUtils.rightFileName, testUtils.rightMultipartFile));
        Assertions.assertThrows(CustomInternalServerErrorException.class, () -> fileService.saveFile(testUtils.wrongFileName, testUtils.wrongMultipartFile));
    }

    @Test
    void testDownloadFile() {
        Assertions.assertThrows(CustomBadRequestException.class, () -> fileService.downloadAsResource(testUtils.wrongFileName));
        Assertions.assertDoesNotThrow(() -> fileService.downloadAsResource(testUtils.rightFileName));
        Assertions.assertEquals(testUtils.emptyIsr, fileService.downloadAsResource(testUtils.rightFileName));
    }

}
