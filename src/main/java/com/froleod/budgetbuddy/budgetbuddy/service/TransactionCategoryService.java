package com.froleod.budgetbuddy.budgetbuddy.service;

import com.froleod.budgetbuddy.budgetbuddy.domain.TransactionCategory;
import com.froleod.budgetbuddy.budgetbuddy.repository.TransactionCategoryRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionCategoryService {
    private final TransactionCategoryRepository transactionCategoryRepository;

    public TransactionCategoryService(TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    @Transactional
    public TransactionCategory createCategory(TransactionCategory category) {
        return transactionCategoryRepository.save(category);
    }

    public List<TransactionCategory> getAllCategories() {
        return transactionCategoryRepository.findAll();
    }

    public TransactionCategory getCategoryById(Long id) {
        return transactionCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @Transactional
    public TransactionCategory updateCategory(Long id, TransactionCategory category) {
        TransactionCategory existingCategory = transactionCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existingCategory.setName(category.getName());
        existingCategory.setType(category.getType());

        return transactionCategoryRepository.save(existingCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!transactionCategoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        transactionCategoryRepository.deleteById(id);
    }
}

