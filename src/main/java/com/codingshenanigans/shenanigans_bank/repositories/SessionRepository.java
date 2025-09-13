package com.codingshenanigans.shenanigans_bank.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Creates a new session in the database.
     * @param userId The user's id.
     * @param refreshToken The refresh token.
     */
    public void create(Long userId, String refreshToken) {
        String query = """
            INSERT INTO sessions (user_id, refresh_token)
            VALUES (?, ?)
        """;

        jdbcTemplate.update(query, userId, refreshToken);
    }
}
