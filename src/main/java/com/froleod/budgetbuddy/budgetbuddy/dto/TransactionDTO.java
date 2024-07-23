package com.froleod.budgetbuddy.budgetbuddy.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;
    private Long categoryId; // только идентификатор категории
}
