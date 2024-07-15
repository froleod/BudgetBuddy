package com.froleod.budgetbuddy.budgetbuddy.service;

import com.froleod.budgetbuddy.budgetbuddy.domain.BankAccount;
import com.froleod.budgetbuddy.budgetbuddy.domain.Budget;
import com.froleod.budgetbuddy.budgetbuddy.domain.FinancialGoal;
import com.froleod.budgetbuddy.budgetbuddy.domain.User;
import com.froleod.budgetbuddy.budgetbuddy.repository.BankAccountRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.BudgetRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.FinancialGoalRepository;
import com.froleod.budgetbuddy.budgetbuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BudgetRepository budgetRepository;
    private final FinancialGoalRepository financialGoalRepository;

    public UserService(UserRepository userRepository, BankAccountRepository bankAccountRepository, BudgetRepository budgetRepository, FinancialGoalRepository financialGoalRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.budgetRepository = budgetRepository;
        this.financialGoalRepository = financialGoalRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow();
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        // получение списка банковских счетов, связанных с пользователем
        Optional<List<BankAccount>> bankAccounts = bankAccountRepository.findByUser(user);

        // удаление каждого банковского счета
        bankAccountRepository.deleteAll(bankAccounts.get());

        Optional<List<Budget>> budgets = budgetRepository.findByUser(user);

        // удаление каждого бюджета
        budgetRepository.deleteAll(budgets.get());

        Optional<List<FinancialGoal>> financialGoals = financialGoalRepository.findByUser(user);

        // удаление каждой финансовой цели
        financialGoalRepository.deleteAll(financialGoals.get());

        // удаление пользователя
        userRepository.delete(user);
    }
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
