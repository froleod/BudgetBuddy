package com.froleod.budgetbuddy.budgetbuddy.repository;

import com.froleod.budgetbuddy.budgetbuddy.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
