package org.WalletHomeWork.entity;

import lombok.Data;

import java.math.BigDecimal;
@Data

public class Wallet {
    private Long id;
    private double balance;
    private Long userId;
}
