package com.froleod.budgetbuddy.budgetbuddy.controller;

import com.froleod.budgetbuddy.budgetbuddy.domain.BankAccount;
import com.froleod.budgetbuddy.budgetbuddy.domain.User;
import com.froleod.budgetbuddy.budgetbuddy.service.BankAccountService;
import com.froleod.budgetbuddy.budgetbuddy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final UserService userService;

    public BankAccountController(BankAccountService bankAccountService, UserService userService) {
        this.bankAccountService = bankAccountService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody BankAccount bankAccount, @RequestParam Long userId) {
        User user = userService.findUserById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        BankAccount createdBankAccount = bankAccountService.createBankAccount(bankAccount, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBankAccount);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BankAccount> updateBankAccount(@PathVariable Long id, @RequestBody BankAccount bankAccount) {
        BankAccount updatedBankAccount = bankAccountService.updateBankAccount(id, bankAccount);
        return ResponseEntity.ok(updatedBankAccount);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        bankAccountService.deleteBankAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/balance/{id}")
    public ResponseEntity<BigDecimal> getBankAccountBalance(@PathVariable Long id) {
        BigDecimal balance = bankAccountService.getBankAccountBalance(id);
        return ResponseEntity.ok(balance);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<BankAccount> getBankAccount(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.getBankAccount(id);
        return ResponseEntity.ok(bankAccount);
    }
}
