package ru.smartjava.backend.utils;

import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;
import ru.smartjava.backend.entity.LoginEntity;

@Configuration
public class JsonGenerator {

    private final Gson gson =  new Gson();

    public String getRightCredentials() {
        return gson.toJson(new LoginEntity("test","password"));
    }

    public String getWrongCredentials() {
        return gson.toJson(new LoginEntity("test","test"));
    }
}
