package ru.smartjava.backend.utils;

import org.springframework.context.annotation.Configuration;
import ru.smartjava.backend.entity.LoginEntity;

import java.io.File;
import java.io.IOException;

@Configuration
public class TestUtils {

    public final String fileName = "876.txt";
    public final String renameFileName = "900.txt";
    public final String filePath = "src\\test\\resources\\cloud-files\\";
    public final String fileFullPath =  filePath + fileName;


    public LoginEntity getRightLogin() {
        return new LoginEntity("test","password");
    }

    public LoginEntity getWrongLogin() {
        return new LoginEntity("test","test");
    }

    public void createTestFile() {
        try {
            new File(fileFullPath).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTestFile() {
        new File(fileFullPath).delete();
    }
}
