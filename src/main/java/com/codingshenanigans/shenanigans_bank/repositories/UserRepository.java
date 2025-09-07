package com.codingshenanigans.shenanigans_bank.repositories;

import com.codingshenanigans.shenanigans_bank.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Checks if a user with the given email already exists in the database.
     * @param email The email to check.
     * @return true if a user with the email exists, false otherwise.
     */
    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, email);
        return count != null && count > 0;
    }

    /**
     * Creates a new user in the database.
     * @param user The user object to create.
     */
    public void createUser(User user) {
        String query = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String password = user.getPassword();
        jdbcTemplate.update(query, firstName, lastName, email, password);
    }
}
