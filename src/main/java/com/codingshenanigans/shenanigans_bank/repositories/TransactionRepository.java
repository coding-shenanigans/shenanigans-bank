package com.codingshenanigans.shenanigans_bank.repositories;

import com.codingshenanigans.shenanigans_bank.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TransactionRowMapper transactionRowMapper;

    @Autowired
    public TransactionRepository(
            JdbcTemplate jdbcTemplate, TransactionRowMapper transactionRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionRowMapper = transactionRowMapper;
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

    /**
     * Gets all the transactions for an account id.
     * @param accountId The account id to filter by.
     * @return A list of transaction objects with the given account id.
     */
    public List<Transaction> list(Long accountId) {
        String query = """
            SELECT *
            FROM transactions
            WHERE account_id = ?
        """;

        return jdbcTemplate.query(query, transactionRowMapper, accountId);
    }
}
