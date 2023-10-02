package ru.smartjava.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.smartjava.backend.entity.LoginEntity;

import java.io.File;
import java.io.IOException;

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

    public String urlLoginPath = "login";
    public String urlLogoutPath = "logout";

    private String getFileFullPath() {
        System.out.println(filePath + fileName);
        return filePath + fileName;
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
        System.out.println(filePath);
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
        new File(getFileFullPath()).delete();
    }
}
