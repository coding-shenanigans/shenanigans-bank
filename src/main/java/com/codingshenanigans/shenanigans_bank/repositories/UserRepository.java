package com.codingshenanigans.shenanigans_bank.repositories;

import com.codingshenanigans.shenanigans_bank.exceptions.ApiException;
import com.codingshenanigans.shenanigans_bank.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    /**
     * Creates a new user in the database.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param email The user's email.
     * @param password The user's password.
     * @return A user object.
     */
    public User create(String firstName, String lastName, String email, String password) {
        String query = """
            INSERT INTO users (first_name, last_name, email, password)
            VALUES (?, ?, ?, ?)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    query, Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, password);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new ApiException("Failed to get user id", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return findById(key.longValue());
    }

    /**
     * Finds a user by their id.
     * @param id The id to search for.
     * @return The user object if found, null otherwise.
     */
    public User findById(Long id) {
        String query = """
            SELECT *
            FROM users
            WHERE id = ?
            LIMIT 1
        """;

        try {
            return jdbcTemplate.queryForObject(query, userRowMapper, id);
        } catch(Exception e) {
            // TODO: log error
            return null;
        }
    }

    /**
     * Finds a user by their email.
     * @param email The email to search for.
     * @return The user object if found, null otherwise.
     */
    public User findByEmail(String email) {
        String query = """
            SELECT *
            FROM users
            WHERE email = ?
            LIMIT 1
        """;

        try {
            return jdbcTemplate.queryForObject(query, userRowMapper, email);
        } catch(Exception e) {
            // TODO: log error
            return null;
        }
    }
}
