package org.WalletHomeWork.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

public class Transaction {
    private Long id;
    private Long userId;
    private LocalDateTime issueDate;
    private Long walletId;
    private TransactionType transactionType;
    private double amount;

    public Transaction( Long userId,Long walletId, TransactionType transactionType, double amount) {
        this.userId = userId;
        this.walletId = walletId;
        this.transactionType = transactionType;
        this.amount = amount;
    }
}
