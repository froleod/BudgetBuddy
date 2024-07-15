package com.froleod.budgetbuddy.budgetbuddy.service;

import com.froleod.budgetbuddy.budgetbuddy.domain.BankAccount;
import com.froleod.budgetbuddy.budgetbuddy.domain.User;
import com.froleod.budgetbuddy.budgetbuddy.repository.BankAccountRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    public BankAccount createBankAccount(BankAccount bankAccount, Long userId) {
        User user = userRepository.findById(userId).get();
        bankAccount.setUser(user);
        return bankAccountRepository.save(bankAccount);
    }

    public BankAccount updateBankAccount(Long bankAccountId, BankAccount updatedBankAccount) {
        BankAccount existingBankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow();
        existingBankAccount.setAccountNumber(updatedBankAccount.getAccountNumber());
        existingBankAccount.setBalance(updatedBankAccount.getBalance());
        return bankAccountRepository.save(existingBankAccount);
    }

    public void deleteBankAccount(Long bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow();
        bankAccountRepository.delete(bankAccount);
    }

    public BigDecimal getBankAccountBalance(Long bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow();
        return bankAccount.getBalance();
    }

    public BankAccount getBankAccount(Long bankAccountId) {
        return bankAccountRepository.findById(bankAccountId).orElseThrow();
    }

}
