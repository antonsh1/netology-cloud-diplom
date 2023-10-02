package ru.smartjava.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.SessionScope;
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
                .withDatabaseName("postgres").withInitScript("schema.sql");
    }

    public static void main(String[] args) {
        SpringApplication.from(BackendApplication::main).with(TestBackendApplication.class).run(args);
    }

//    @Test
//    public void asdfasf() {
//        given()
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/login")
//                .then()
//                .statusCode(401);
////        ResponseEntity<String> forFirstEntity = restTemplate.getForEntity("http://localhost:8888/login", String.class);
////        System.out.println(forFirstEntity.getStatusCode());
////        Assertions.assertTrue(Objects.requireNonNull(forFirstEntity.getStatusCode()).contains("is dev"));
//    }
}
