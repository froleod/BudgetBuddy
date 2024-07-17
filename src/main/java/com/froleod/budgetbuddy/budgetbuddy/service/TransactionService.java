package com.froleod.budgetbuddy.budgetbuddy.service;

import com.froleod.budgetbuddy.budgetbuddy.domain.BankAccount;
import com.froleod.budgetbuddy.budgetbuddy.domain.Transaction;
import com.froleod.budgetbuddy.budgetbuddy.repository.BankAccountRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;


    public TransactionService(TransactionRepository transactionRepository, BankAccountService bankAccountService) {
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
    }
    @Transactional
    public Transaction create(Transaction transaction) throws IllegalAccessException {
        BankAccount bankAccount = transaction.getBankAccount();
        if(bankAccount == null) {
            throw new IllegalAccessException("Транзакция должна быть привязана к банковскому аккаунту");
        }

        transaction = transactionRepository.save(transaction);
        bankAccountService.updateBalance(transaction.getBankAccount().getId(), transaction.getAmount(), transaction.getCategory().getType());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Transaction updateTransaction(Long id, Transaction transaction) {
        Transaction transactionToUpdate = transactionRepository.findById(id).orElseThrow();
        transactionToUpdate.setAmount(transaction.getAmount());
        transactionToUpdate.setDescription(transaction.getDescription());
        transactionToUpdate.setTransactionDate(transaction.getTransactionDate());
        transactionToUpdate.setCategory(transaction.getCategory());
        transactionToUpdate.setCategory(transaction.getCategory());
        return transactionRepository.save(transactionToUpdate);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
