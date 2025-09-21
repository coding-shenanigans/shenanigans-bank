package com.codingshenanigans.shenanigans_bank.dtos;

import com.codingshenanigans.shenanigans_bank.models.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OpenAccountRequest {
    @NotNull(message = "The balance is required")
    @DecimalMin(value = "0", message = "The initial balance cannot be negative")
    private BigDecimal balance;

    @NotNull(message = "The account type is required")
    private AccountType type;

    public OpenAccountRequest(BigDecimal balance, AccountType type) {
        this.balance = balance;
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
}
