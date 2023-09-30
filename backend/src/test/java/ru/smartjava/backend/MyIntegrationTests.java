package ru.smartjava.backend;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.ErrorMessage;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.entity.FileToRename;
import ru.smartjava.backend.entity.TokenMessage;
import ru.smartjava.backend.utils.TestUtils;

import java.io.File;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestBackendApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyIntegrationTests {

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private PostgreSQLContainer postgres;

    private TokenMessage tokenMessage;

    @BeforeAll
    void start() {
        testUtils.createTestFile();
        postgres.start();
    }

    @AfterAll
    void stop() {
        postgres.stop();
        testUtils.deleteTestFile();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:8888";
    }

    @Order(10)
    @Test
    public void testLogin() {

        Response response = given()
                .contentType(ContentType.JSON)
                .when().post(Constants.urlLoginPath)
                .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(testUtils.getWrongLogin())
                        .when().post(Constants.urlLoginPath).then()
                        .extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response = given()
                .contentType(ContentType.JSON)
                .body(testUtils.getRightLogin())
                .when().post(Constants.urlLoginPath)
                .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertInstanceOf(TokenMessage.class, response.as(TokenMessage.class));
        tokenMessage = response.as(TokenMessage.class);

    }

    @Order(20)
    @Test
    public void testUploadFile() {

        Response response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, "")
                        .when().post(Constants.urlFilePath)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().post(Constants.urlFilePath)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .multiPart(new File(testUtils.fileFullPath))
                        .when().post(Constants.urlFilePath)
                        .then().extract().response();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));


        response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .multiPart(new File(testUtils.fileFullPath))
                        .when().post(Constants.urlFilePath + "?filename=" + testUtils.fileName)
                        .then().extract().response();
        System.out.println(response.getBody().prettyPrint());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertEquals("", response.getBody().print());

    }

    @Order(30)
    @Test
    public void testList() {

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlListPath + "?limit=3")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertInstanceOf(FileItem[].class, response.as(FileItem[].class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, "")
                        .when().get(Constants.urlListPath + "?limit=3")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlFilePath).then()
                        .extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));
//        TODO ошибка 500 возможно ли воспроизвести

    }

    @Order(40)
    @Test
    public void testDownloadFile() {

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, "")
                        .when().get(Constants.urlFilePath)
                        .then().extract().response();
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlFilePath)
                        .then().extract().response();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlFilePath + "?filename=6.txt")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlFilePath + "?filename=" + testUtils.fileName)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
//        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));
    }

    @Order(50)
    @Test
    public void testRenameFile() {

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, "")
                        .when().put(Constants.urlFilePath)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().put(Constants.urlFilePath)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().put(Constants.urlFilePath + "?filename=" + testUtils.fileName)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(new FileToRename(testUtils.renameFileName))
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().put(Constants.urlFilePath + "?filename=xxx.txt")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ErrorMessage.class, response.as(ErrorMessage.class));

        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(new FileToRename(testUtils.renameFileName))
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().put(Constants.urlFilePath + "?filename" + testUtils.fileName)
                        .then().extract().response();

        System.out.println(response.getBody().prettyPrint());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        given()
                .contentType(ContentType.JSON)
                .body(new FileToRename(testUtils.fileName))
                .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                .when().put(Constants.urlFilePath + "?filename=" + testUtils.renameFileName);

    }

    @Order(60)
    @Test
    public void testLogout() {

        given()
                .contentType(ContentType.JSON)
                .cookie(Constants.authTokenName, "")
                .when().post(Constants.urlLogoutPath)
                .then().statusCode(HttpStatus.UNAUTHORIZED.value());

        given()
                .contentType(ContentType.JSON)
                .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                .when().post(Constants.urlLogoutPath)
                .then().statusCode(HttpStatus.OK.value());
    }
}
