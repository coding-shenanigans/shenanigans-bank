package com.codingshenanigans.shenanigans_bank.models;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
        Long id,
        Long accountId,
        String title,
        BigDecimal amount,
        BigDecimal newBalance,
        Instant createdAt,
        Instant updatedAt
) {}
