package ru.smartjava.backend;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.ResponseMessage;
import ru.smartjava.backend.entity.FileItem;
import ru.smartjava.backend.entity.FileToRename;
import ru.smartjava.backend.entity.TokenMessage;
import ru.smartjava.backend.utils.TestUtils;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestBackendApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTests {

    @LocalServerPort
    private Long port;

    @Autowired
    private TestUtils testUtils;

    private TokenMessage tokenMessage;

    @BeforeAll
    void start() {
        testUtils.createTestFile();
    }

    @AfterAll
    void stop() {
        testUtils.deleteTestFile();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Order(10)
    @Test
    public void testLogin() {

        //Пустой запрос
        Response response = given()
                .contentType(ContentType.JSON)
                .when().post(testUtils.urlLoginPath)
                .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Неверные данные авторизации
        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(testUtils.getWrongLogin())
                        .when().post(testUtils.urlLoginPath).then()
                        .extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Верные данные авторизации
        response = given()
                .contentType(ContentType.JSON)
                .body(testUtils.getRightLogin())
                .when().post(testUtils.urlLoginPath)
                .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertInstanceOf(TokenMessage.class, response.as(TokenMessage.class));
        //Запоминаем токен, для авторизации в дальнейших тестах
        tokenMessage = response.as(TokenMessage.class);

    }

    @Order(20)
    @Test
    public void testUploadFile() {

        //Неверные данные авторизации
        Response response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, "")
                        .when().post(Constants.urlFilePath)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Не все параметры запроса проставлены
        response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().post(Constants.urlFilePath)
                        .then().extract().response();
        System.out.println(response.getBody().prettyPrint());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Не все параметры запроса проставлены
        response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .multiPart(testUtils.getTestFile())
                        .when().post(Constants.urlFilePath)
                        .then().extract().response();
        System.out.println(response.getBody().prettyPrint());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Верные параметры запроса
        response =
                given()
                        .contentType(ContentType.MULTIPART)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .multiPart(testUtils.getTestFile())
                        .when().post(Constants.urlFilePath + "?filename=" + testUtils.fileName)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertEquals("", response.getBody().print());

    }

    @Order(30)
    @Test
    public void testList() {

        //Неверные данные авторизации
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, "")
                        .when().get(Constants.urlListPath + "?limit=3")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Не все параметры запроса проставлены
        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlFilePath).then()
                        .extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Верные параметры запроса
        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlListPath + "?limit=3")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertInstanceOf(FileItem[].class, response.as(FileItem[].class));
    }

    @Order(40)
    @Test
    public void testDownloadFile() {

        //Неверные данные авторизации
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, "")
                        .when().get(Constants.urlFilePath)
                        .then().extract().response();
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Не все параметры запроса проставлены
        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlFilePath)
                        .then().extract().response();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Такого файла нет
        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlFilePath + "?filename=6.txt")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Верные параметры запроса
        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().get(Constants.urlFilePath + "?filename=" + testUtils.fileName)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

    }

    @Order(50)
    @Test
    public void testRenameFile() {

        //Неверные данные авторизации
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, "")
                        .when().put(Constants.urlFilePath)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Не все параметры запроса проставлены
        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().put(Constants.urlFilePath)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Не все параметры запроса проставлены
        response =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().put(Constants.urlFilePath + "?filename=" + testUtils.fileName)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Такого файла нет
        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(new FileToRename(testUtils.renameFileName))
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().put(Constants.urlFilePath + "?filename=xxx.txt")
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assertions.assertInstanceOf(ResponseMessage.class, response.as(ResponseMessage.class));

        //Верные параметры запроса
        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(new FileToRename(testUtils.renameFileName))
                        .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                        .when().put(Constants.urlFilePath + "?filename=" + testUtils.fileName)
                        .then().extract().response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Order(60)
    @Test
    public void testLogout() {

        //Неверные данные авторизации
        given()
                .contentType(ContentType.JSON)
                .cookie(Constants.authTokenName, "")
                .when().post(testUtils.urlLogoutPath)
                .then().statusCode(HttpStatus.UNAUTHORIZED.value());

        //Верный токен
        given()
                .contentType(ContentType.JSON)
                .cookie(Constants.authTokenName, tokenMessage.getAuthtoken())
                .when().post(testUtils.urlLogoutPath)
                .then().statusCode(HttpStatus.OK.value());
    }
}
