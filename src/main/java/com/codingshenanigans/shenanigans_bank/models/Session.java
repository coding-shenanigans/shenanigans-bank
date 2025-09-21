package com.codingshenanigans.shenanigans_bank.models;

import java.time.Instant;

public record Session(
        Long id,
        Long userId,
        String refreshToken,
        Instant createdAt,
        Instant updatedAt
) {}
