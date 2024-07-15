package com.froleod.budgetbuddy.budgetbuddy.repository;

import com.froleod.budgetbuddy.budgetbuddy.domain.Budget;
import com.froleod.budgetbuddy.budgetbuddy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<List<Budget>> findByUser(User user);
}
