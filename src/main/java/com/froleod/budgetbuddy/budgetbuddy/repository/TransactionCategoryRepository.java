package com.froleod.budgetbuddy.budgetbuddy.repository;

import com.froleod.budgetbuddy.budgetbuddy.domain.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
}
