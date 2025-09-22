package com.codingshenanigans.shenanigans_bank.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AccountDepositRequest {
    @NotBlank(message = "The title is required")
    private String title;

    @NotNull(message = "The amount is required")
    @DecimalMin(value = "0", message = "The amount cannot be negative")
    private BigDecimal amount;

    public AccountDepositRequest(BigDecimal amount, String title) {
        this.amount = amount;
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
