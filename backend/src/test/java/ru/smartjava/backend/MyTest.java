package ru.smartjava.backend;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static io.restassured.RestAssured.given;

@SpringBootTest
public class MyTest {

    @Container
    PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withExposedPorts(5432)
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withDatabaseName("postgres").withInitScript("schema.sql");

//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//    }
//
//    @AfterAll
//    static void afterAll() {
//        postgres.stop();
//    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:8888";
    }

    @Test
    public void asdfasf() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/login")
                .then()
                .statusCode(401);
//        ResponseEntity<String> forFirstEntity = restTemplate.getForEntity("http://localhost:8888/login", String.class);
//        System.out.println(forFirstEntity.getStatusCode());
//        Assertions.assertTrue(Objects.requireNonNull(forFirstEntity.getStatusCode()).contains("is dev"));
    }

}
