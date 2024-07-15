package com.froleod.budgetbuddy.budgetbuddy.service;

import com.froleod.budgetbuddy.budgetbuddy.domain.BankAccount;
import com.froleod.budgetbuddy.budgetbuddy.domain.User;
import com.froleod.budgetbuddy.budgetbuddy.repository.BankAccountRepository;
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

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Epic("Интеграционный тест")
@Feature("Тест на CRUD операции банковского аккаунта")
public class BankAccountServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    User user;
    BankAccount bankAccount;
    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        userRepository.save(user);

        bankAccount = new BankAccount();
        bankAccount.setAccountNumber("1111111");
        bankAccount.setBalance(BigDecimal.valueOf(1000.00));
        bankAccount.setUser(user);
        bankAccountRepository.save(bankAccount);
    }

    @AfterEach
    public void cleanup() {
        bankAccountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @Description("Тест создания банковского аккаунта")
    void testCreateBankAccount() {
        System.out.println(user.getId());
        System.out.println(bankAccount.toString());
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .contentType(ContentType.JSON)
                .body(bankAccount)
                .queryParam("userId", user.getId())
                .when()
                .post("/api/bank-accounts/create")
                .then()
                .extract().response();

        Assertions.assertEquals(201, response.getStatusCode(), "Ошибка статус кода");

        BankAccount resultObject = new Gson().fromJson(response.getBody().asString(), BankAccount.class);

        Assertions.assertEquals("1111111", resultObject.getAccountNumber(), "Ошибка чтения номера счета");
        Assertions.assertEquals(BigDecimal.valueOf(1000.00), resultObject.getBalance(), "Ошибка чтения баланса");
    }

    @Test
    @Description("Тест обновления банковского аккаунта")
    void testUpdateBankAccount() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();


        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("222222222");
        bankAccount.setBalance(BigDecimal.valueOf(1500.00));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(bankAccount)
                .pathParam("id", "1")
                .when()
                .put("/api/bank-accounts/update/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.getStatusCode(), "Ошибка статус кода");

        BankAccount resultObject = new Gson().fromJson(response.getBody().asString(), BankAccount.class);

        Assertions.assertEquals("222222222", resultObject.getAccountNumber(), "Ошибка чтения номера счета");
        Assertions.assertEquals(BigDecimal.valueOf(1500.00), resultObject.getBalance(), "Ошибка чтения баланса");
    }

    @Test
    @Description("Тест удаления банковского аккаунта")
    void testDeleteBankAccount() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();


        Response response = given()
                .pathParam("id", bankAccount.getId())
                .when()
                .delete("/api/bank-accounts/delete/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(204, response.getStatusCode(), "Ошибка статус кода");
    }

    @Test
    @Description("Тест получения баланса банковского аккаунта по id")
    void testGetBankAccountBalance() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();


        Response response = given()
                .pathParam("id", bankAccount.getId())
                .when()
                .get("/api/bank-accounts/get/balance/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.getStatusCode(), "Ошибка статус кода");

        BigDecimal resultObject = new Gson().fromJson(response.getBody().asString(), BigDecimal.class);

        Assertions.assertEquals(1000.00, resultObject.doubleValue(), "Ошибка чтения баланса");
    }
}