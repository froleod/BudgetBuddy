package com.froleod.budgetbuddy.budgetbuddy.service;

import com.froleod.budgetbuddy.budgetbuddy.domain.User;
import com.froleod.budgetbuddy.budgetbuddy.repository.UserRepository;
import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.google.common.reflect.TypeToken;

import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Epic("Интеграционный тест")
@Feature("Тест на CRUD операции пользователя")
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    User user;
    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        userRepository.save(user);
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @Description("Тест создания пользователя")
    void testCreateUser() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/user/create")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.getStatusCode(), "Ошибка статус кода");

        User resultObject = new Gson().fromJson(response.getBody().asString(), User.class);

        Assertions.assertEquals("user", resultObject.getUsername(), "Ошибка чтения имени пользователя");
        Assertions.assertEquals("pass", resultObject.getPassword(), "Ошибка чтения пароля");
    }

    @Test
    @Description("Тест получения пользователя по имени")
    void testGetUser() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .pathParam("username", "user")
                .when()
                .get("/api/user/get/{username}")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.getStatusCode(), "Ошибка статус кода");

        User resultObject = new Gson().fromJson(response.getBody().asString(), User.class);

        Assertions.assertEquals("user", resultObject.getUsername(), "Ошибка чтения имени пользователя");
        Assertions.assertEquals("pass", resultObject.getPassword(), "Ошибка чтения пароля");
    }

    @Test
    @Description("Тест обновления пользователя")
    void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setUsername("newuser");
        updatedUser.setPassword("newpass");

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .pathParam("id", user.getId())
                .when()
                .put("/api/user/update/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.getStatusCode(), "Ошибка статус кода");

        User resultObject = new Gson().fromJson(response.getBody().asString(), User.class);

        Assertions.assertEquals("newuser", resultObject.getUsername(), "Ошибка чтения имени пользователя");
        Assertions.assertEquals("newpass", resultObject.getPassword(), "Ошибка чтения пароля");
    }

    @Test
    @Description("Тест удаления пользователя")
    void testDeleteUser() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .pathParam("id", user.getId())
                .when()
                .delete("/api/user/delete/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.getStatusCode(), "Ошибка статус кода");
    }

    @Test
    @Description("Тест получения всех пользователей")
    void testGetAllUsers() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .when()
                .get("/api/user/getAll")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.getStatusCode(), "Ошибка статус кода");

        List<User> resultObjects = new Gson().fromJson(response.getBody().asString(), new TypeToken<List<User>>() {}.getType());


        Assertions.assertEquals(1, resultObjects.size(), "Ошибка количества пользователей");
        Assertions.assertEquals("user", resultObjects.getFirst().getUsername(), "Ошибка чтения имени пользователя");
        Assertions.assertEquals("pass", resultObjects.getFirst().getPassword(), "Ошибка чтения пароля");
    }
}