package ru.smartjava.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


@TestConfiguration(proxyBeanMethods = false)
public class TestBackendApplication {


    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("postgres").withInitScript("sql/schema-test.sql");
    }

    public static void main(String[] args) {
        SpringApplication.from(BackendApplication::main).with(TestBackendApplication.class).run(args);
    }

}
