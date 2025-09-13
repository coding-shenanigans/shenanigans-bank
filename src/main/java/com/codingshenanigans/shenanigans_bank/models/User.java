package com.codingshenanigans.shenanigans_bank.models;

import java.time.Instant;

public record User(
        Long id,
        String firstName,
        String lastName,
        String email,
        String password,
        Instant createdAt,
        Instant updatedAt
) {
}
