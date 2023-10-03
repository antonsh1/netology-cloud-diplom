package ru.smartjava.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mock.web.MockMultipartFile;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.entity.LoginEntity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestUtils {

    @Value("${cloud-settings.test.filename}")
    public String fileName;

    @Value("${cloud-settings.test.rename-filename}")
    public String renameFileName;

    @Value("${cloud-settings.filestorepath}")
    public String filePath;

    @Value("${cloud-settings.test.login}")
    public String testUserName;

    @Value("${cloud-settings.test.password}")
    public String testUserPassword;

    public final String urlLoginPath = "login";
    public final String urlLogoutPath = "logout";

    public final String rightFileName =  "rightFile";
    public final String wrongFileName =  "wrongFile";

    public final MockMultipartFile rightMultipartFile =
            new MockMultipartFile(rightFileName, rightFileName, "text/plain", (byte[]) null);

    public final MockMultipartFile wrongMultipartFile =
            new MockMultipartFile(wrongFileName, wrongFileName, "text/plain", (byte[]) null);

    public final InputStreamResource emptyIsr = new InputStreamResource(new ByteArrayInputStream(new byte[0]));

    public final Integer fileListLimit = 3;

    public final List<File> fileList = new ArrayList<>(List.of(
            new File("1"),
            new File("2"),
            new File("3")
    ));
    public final List<FileItem> fileItems = new ArrayList<>(List.of(
            new FileItem("1", 0),
            new FileItem("2", 0),
            new FileItem("3", 0)
    ));

    private String getFileFullPath() {
        return filePath + fileName;
    }

    private String getRenamedFileFullPath() {
        return filePath + renameFileName;
    }
    public File getTestFile() {
        return new File(getFileFullPath());
    }

    public LoginEntity getRightLogin() {
        return new LoginEntity(testUserName,testUserPassword);
    }

    public LoginEntity getWrongLogin() {
        return new LoginEntity(testUserName,"");
    }

    public void createTestFile() {
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
        try {
            new File(getFileFullPath()).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTestFile() {
        new File(getRenamedFileFullPath()).delete();
    }
}
