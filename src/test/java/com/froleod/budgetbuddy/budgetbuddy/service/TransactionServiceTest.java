package com.froleod.budgetbuddy.budgetbuddy.service;

//import com.froleod.budgetbuddy.budgetbuddy.adapter.LocalDateTimeTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.froleod.budgetbuddy.budgetbuddy.domain.*;
import com.froleod.budgetbuddy.budgetbuddy.repository.BankAccountRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.TransactionCategoryRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.TransactionRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Epic("Интеграционный тест")
@Feature("Тест на CRUD операции транзакций")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private UserRepository userRepository;

    User user;
    BankAccount bankAccount;
    TransactionCategory transactionCategory;
    Transaction transaction;
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        userRepository.save(user);

        bankAccount = new BankAccount();
        bankAccount.setAccountNumber("1111111");
        bankAccount.setBalance(BigDecimal.valueOf(1000.0));
        bankAccount.setUser(user);
        bankAccountRepository.save(bankAccount);

        transactionCategory = new TransactionCategory();
        transactionCategory.setName("Groceries");
        transactionCategory.setType(TransactionType.EXPENSE);
        transactionCategoryRepository.save(transactionCategory);

        transaction = new Transaction();
        transaction.setDescription("Test Transaction");
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setTransactionDate(LocalDateTime.now().plusDays(14));
        transaction.setCategory(transactionCategory);
        transaction.setBankAccount(bankAccount);
        transactionRepository.save(transaction);
    }

    @AfterEach
    public void cleanup() {
        transactionRepository.deleteAllInBatch();
        transactionCategoryRepository.deleteAllInBatch();
        bankAccountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @Description("Тест создания транзакции")
    void testCreateTransaction() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();

        TransactionCategory newTransactionCategory = new TransactionCategory();
        newTransactionCategory.setName("Entertainment");
        newTransactionCategory.setType(TransactionType.EXPENSE);
        transactionCategoryRepository.save(newTransactionCategory);

        Transaction newTransaction = new Transaction();
        newTransaction.setDescription("New Transaction");
        newTransaction.setAmount(BigDecimal.valueOf(50.0));
        newTransaction.setTransactionDate(LocalDateTime.now().plusDays(13));
        newTransaction.setCategory(newTransactionCategory);
        newTransaction.setBankAccount(bankAccount);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(newTransaction)
                .when()
                .post("/api/transactions/add")
                .then()
                .extract().response();

        Assertions.assertEquals(201, response.getStatusCode(), "Ошибка статус кода");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Transaction resultTransaction = objectMapper.readValue(response.getBody().asString(), Transaction.class);

        Assertions.assertEquals("New Transaction", resultTransaction.getDescription(), "Ошибка чтения описания транзакции");
        Assertions.assertEquals(BigDecimal.valueOf(50.0), resultTransaction.getAmount(), "Ошибка чтения суммы транзакции");
        Assertions.assertEquals("Entertainment", resultTransaction.getCategory().getName(), "Ошибка чтения категории транзакции");
    }

//    @Test
//    @Description("Тест обновления транзакции")
//    void testUpdateTransaction() throws JsonProcessingException {
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.registerModule(new Hibernate5Module());
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = port;
//        RestAssured.useRelaxedHTTPSValidation();
//        transaction.setDescription("Updated Transaction");
//        transaction.setAmount(BigDecimal.valueOf(75.0));
//
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .body(transaction)
//                .pathParam("id", transaction.getId())
//                .when()
//                .put("/api/transactions/update/{id}")
//                .then()
//                .extract().response();
//
//        Assertions.assertEquals(200, response.getStatusCode(), "Ошибка статус кода");
//
//        Transaction resultTransaction = objectMapper.readValue(response.getBody().asString(), Transaction.class);
//
//        Assertions.assertEquals("Updated Transaction", resultTransaction.getDescription(), "Ошибка чтения описания транзакции");
//        Assertions.assertEquals(BigDecimal.valueOf(75.0), resultTransaction.getAmount(), "Ошибка чтения суммы транзакции");
//    }
//
//    @Test
//    @Description("Тест получения транзакции по id")
//    void testGetTransactionById() throws JsonProcessingException {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = port;
//        RestAssured.useRelaxedHTTPSValidation();
//        Response response = given()
//                .pathParam("id", transaction.getId())
//                .when()
//                .get("/api/transactions/get/{id}")
//                .then()
//                .extract().response();
//
//        Assertions.assertEquals(200, response.getStatusCode(), "Ошибка статус кода");
//
//        Transaction resultTransaction = objectMapper.readValue(response.getBody().asString(), Transaction.class);
//
//        Assertions.assertEquals("Test Transaction", resultTransaction.getDescription(), "Ошибка чтения описания транзакции");
//        Assertions.assertEquals(BigDecimal.valueOf(100.0), resultTransaction.getAmount(), "Ошибка чтения суммы транзакции");
//        Assertions.assertEquals("Groceries", resultTransaction.getCategory().getName(), "Ошибка чтения категории транзакции");
//    }

    @Test
    @Description("Тест удаления транзакции")
    void testDeleteTransaction() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();

        Response response = given()
                .pathParam("id", transaction.getId())
                .when()
                .delete("/api/transactions/delete/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(204, response.getStatusCode(), "Ошибка статус кода");
    }
}