package com.froleod.budgetbuddy.budgetbuddy.controller;

import com.froleod.budgetbuddy.budgetbuddy.domain.TransactionCategory;
import com.froleod.budgetbuddy.budgetbuddy.service.TransactionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class TransactionCategoryController {

    private final TransactionCategoryService transactionCategoryService;

    public TransactionCategoryController(TransactionCategoryService transactionCategoryService) {
        this.transactionCategoryService = transactionCategoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionCategory> createCategory(@RequestBody TransactionCategory category) {
        TransactionCategory createdCategory = transactionCategoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TransactionCategory>> getAllCategories() {
        List<TransactionCategory> categories = transactionCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TransactionCategory> getCategoryById(@PathVariable Long id) {
        TransactionCategory category = transactionCategoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TransactionCategory> updateCategory(@PathVariable Long id, @RequestBody TransactionCategory category) {
        TransactionCategory updatedCategory = transactionCategoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        transactionCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
