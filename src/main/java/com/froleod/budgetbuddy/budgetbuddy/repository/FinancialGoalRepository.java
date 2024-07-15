package com.froleod.budgetbuddy.budgetbuddy.repository;

import com.froleod.budgetbuddy.budgetbuddy.domain.FinancialGoal;
import com.froleod.budgetbuddy.budgetbuddy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
    Optional<List<FinancialGoal>> findByUser(User user);
}
