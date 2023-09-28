package ru.smartjava.backend;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.controller.FileController;
import ru.smartjava.backend.entity.ErrorMessage;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.entity.LoginEntity;
import ru.smartjava.backend.entity.TokenMessage;
import ru.smartjava.backend.utils.JsonGenerator;

import java.io.File;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestBackendApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyIntegrationTests {

    @Autowired
    private JsonGenerator jsonGenerator;

    @Autowired
    private PostgreSQLContainer postgres;

    private TokenMessage tokenMessage;

    @BeforeAll
    void start() {
        postgres.start();
    }

    @AfterAll
    void stop() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:8888";
    }

    @Order(1)
    @Test
    public void testLogin() {
        System.out.println("LOGIN");
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .post("/login").then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(new LoginEntity("test", "test"))
                        .when()
                        .post("/login").then()
                        .extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response = given()
                .contentType(ContentType.JSON)
                .body(jsonGenerator.getRightCredentials())
                .when()
                .post("/login").then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertInstanceOf(TokenMessage.class, response.as(TokenMessage.class));
        tokenMessage = response.as(TokenMessage.class);

    }

    @Order(2)
    @Test
    public void testUploadFile() {
        System.out.println("UPLOAD");
        Response response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, "")
                        .when()
                        .post("/file")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when()
                        .post("/file")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .multiPart(new File("C:\\Development\\Projects\\Netology\\netology-cloud-diplom\\backend\\src\\test\\resources\\876.txt"))
                        .when()
                        .post("/file")
                        .then().extract().response();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));


        response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .multiPart(new File("C:\\Development\\Projects\\Netology\\netology-cloud-diplom\\backend\\src\\test\\resources\\876.txt"))
                        .when()
                        .post("/file?filename=876.txt")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
//        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

    }

    @Order(3)
    @Test
    public void testList() {
        System.out.println("LIST");
//        System.out.println(tokenMessage);
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get("/list?limit=3")
                        .then().extract().response();

//        System.out.println(response.getBody().prettyPrint());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertInstanceOf(FileItem[].class, response.as(FileItem[].class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, "")
                        .when().get("/list?limit=3")
                        .then()
                        .extract().response();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get("/list").then()
                        .extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

//        TODO ошибка 500 возможно ли воспроизвести
    }

    @Order(4)
    @Test
    public void testDownloadFile() {
        System.out.println("DOWNLOAD");
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, "")
                        .when()
                        .get("/file")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when()
                        .get("/file")
                        .then().extract().response();
        System.out.println(response.getBody().prettyPrint());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when()
                        .get("/file?filename=876.txt")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
//        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));
    }

    @Order(10)
    @Test
    public void testLogout() {
        System.out.println("LOGOUT");
        given()
                .contentType(ContentType.JSON)
                .cookie(Constants.authTokenName, "")
                .when().post("/logout").then().statusCode(HttpStatus.UNAUTHORIZED.value());
        given()
                .contentType(ContentType.JSON)
                .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                .when().post("/logout").then().statusCode(HttpStatus.OK.value());
    }
}
