package com.froleod.budgetbuddy.budgetbuddy.service;

import com.froleod.budgetbuddy.budgetbuddy.domain.BankAccount;
import com.froleod.budgetbuddy.budgetbuddy.domain.Transaction;
import com.froleod.budgetbuddy.budgetbuddy.domain.TransactionCategory;
import com.froleod.budgetbuddy.budgetbuddy.dto.TransactionDTO;
import com.froleod.budgetbuddy.budgetbuddy.repository.BankAccountRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.TransactionCategoryRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;
    private final TransactionCategoryRepository transactionCategoryRepository;


    public TransactionService(TransactionRepository transactionRepository, BankAccountService bankAccountService, TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    @Transactional
    public Transaction create(Transaction transaction) throws IllegalAccessException {
        BankAccount bankAccount = transaction.getBankAccount();
        if (bankAccount == null) {
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
    public Transaction updateTransaction(Long id, TransactionDTO transactionDTO) {

        Transaction transactionToUpdate = getTransactionById(id);

        TransactionCategory category = transactionCategoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        transactionToUpdate.setAmount(transactionDTO.getAmount());
        transactionToUpdate.setDescription(transactionDTO.getDescription());
        transactionToUpdate.setTransactionDate(transactionDTO.getTransactionDate());

        transactionToUpdate.setCategory(category);
        return transactionRepository.save(transactionToUpdate);
    }


    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
