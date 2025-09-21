package com.codingshenanigans.shenanigans_bank.repositories;

import com.codingshenanigans.shenanigans_bank.exceptions.ApiException;
import com.codingshenanigans.shenanigans_bank.models.Account;
import com.codingshenanigans.shenanigans_bank.models.AccountStatus;
import com.codingshenanigans.shenanigans_bank.models.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AccountRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AccountRowMapper accountRowMapper;

    @Autowired
    public AccountRepository(JdbcTemplate jdbcTemplate, AccountRowMapper accountRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountRowMapper = accountRowMapper;
    }

    /**
     * Creates a new account in the database.
     * @param userId The owner's id.
     * @param type The account type.
     * @return An account object.
     */
    public Account create(long userId, BigDecimal balance, AccountType type, AccountStatus status) {
        String query = """
            INSERT INTO accounts (user_id, balance, type, status)
            VALUES (?, ?, ?, ?)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    query, Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, userId);
            ps.setBigDecimal(2, balance);
            ps.setString(3, type.toString());
            ps.setString(4, status.toString());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new ApiException("Failed to get account id", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return findById(key.longValue());
    }

    /**
     * Finds an account by its id.
     * @param id The id to search for.
     * @return The account object if found, null otherwise.
     */
    public Account findById(Long id) {
        String query = """
            SELECT *
            FROM accounts
            WHERE id = ?
            LIMIT 1
        """;

        try {
            return jdbcTemplate.queryForObject(query, accountRowMapper, id);
        } catch(Exception e) {
            // TODO: log error
            return null;
        }
    }

    public List<Account> list(Long userId) {
        String query = """
            SELECT *
            FROM accounts
            WHERE user_id = ?
        """;

        return jdbcTemplate.query(query, accountRowMapper, userId);
    }
}
