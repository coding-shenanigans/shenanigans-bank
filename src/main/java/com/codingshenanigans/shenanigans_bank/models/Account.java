package com.codingshenanigans.shenanigans_bank.models;

import java.math.BigDecimal;
import java.time.Instant;

public record Account(
        Long id,
        Long userId,
        BigDecimal balance,
        AccountType type,
        AccountStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
