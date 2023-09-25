package ru.smartjava.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.nio.charset.StandardCharsets;

@Configuration
public class TestEnv {

    private final Environment env;

    public final String userNotFound;

    public TestEnv(Environment env) {
        this.env = env;
        this.userNotFound = env.getProperty("cloud-settings.messages.usernotfound");
    }
}
