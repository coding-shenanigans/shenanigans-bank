package com.codingshenanigans.shenanigans_bank.repositories;

import com.codingshenanigans.shenanigans_bank.exceptions.ApiException;
import com.codingshenanigans.shenanigans_bank.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SessionRowMapper sessionRowMapper;

    @Autowired
    public SessionRepository(JdbcTemplate jdbcTemplate, SessionRowMapper sessionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionRowMapper = sessionRowMapper;
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

    /**
     * Finds a session by its id.
     * @param id The id to search for.
     * @return The session object if found, null otherwise.
     */
    public Session findById(Long id) {
        String query = """
            SELECT *
            FROM sessions
            WHERE id = ?
            LIMIT 1
        """;

        try {
            return jdbcTemplate.queryForObject(query, sessionRowMapper, id);
        } catch(Exception e) {
            // TODO: log error
            return null;
        }
    }

    /**
     * Finds a session by its refresh token.
     * @param refreshToken The refresh token to search for.
     * @return The session object if found, null otherwise.
     */
    public Session findByRefreshToken(String refreshToken) {
        String query = """
            SELECT *
            FROM sessions
            WHERE refresh_token = ?
            LIMIT 1
        """;

        try {
            return jdbcTemplate.queryForObject(query, sessionRowMapper, refreshToken);
        } catch(Exception e) {
            // TODO: log error
            return null;
        }
    }

    /**
     * Updates the session's refresh token and returns the updated object.
     * @param id The session's id.
     * @param refreshToken The new refresh token value.
     */
    public void updateRefreshToken(Long id, String refreshToken) {
        String query = """
            UPDATE sessions
            SET refresh_token = ?
            WHERE id = ?
        """;

        int rowsAffected = jdbcTemplate.update(query, refreshToken, id);
        if (rowsAffected <= 0) {
            throw new ApiException("Failed to update session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
