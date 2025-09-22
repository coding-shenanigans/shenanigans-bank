package com.codingshenanigans.shenanigans_bank.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Creates a new transaction in the database.
     * @param accountId The account this transaction belongs to.
     * @param title The title for the transaction.
     * @param amount The amount for the transaction.
     * @param newBalance The new account balance after the transaction.
     */
    public void create(Long accountId, String title, BigDecimal amount, BigDecimal newBalance) {
        String query = """
            INSERT INTO transactions (account_id, title, amount, new_balance)
            VALUES (?, ?, ?, ?)
        """;

        jdbcTemplate.update(query, accountId, title, amount, newBalance);
    }
}
