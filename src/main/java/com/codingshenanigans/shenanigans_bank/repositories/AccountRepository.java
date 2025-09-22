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
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AccountRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AccountRowMapper accountRowMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public AccountRepository(
            JdbcTemplate jdbcTemplate,
            AccountRowMapper accountRowMapper,
            TransactionRepository transactionRepository,
            TransactionTemplate transactionTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountRowMapper = accountRowMapper;
        this.transactionRepository = transactionRepository;
        this.transactionTemplate = transactionTemplate;
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
     * @param id The account id to search for.
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

    /**
     * Closes the account with the given id.
     * @param accountId The account id to close.
     * @return The updated account object.
     */
    public Account close(Long accountId) {
        String query = """
            UPDATE accounts
            SET status = ?
            WHERE id = ?
        """;

        int rowsAffected = jdbcTemplate.update(query, AccountStatus.CLOSED.toString(), accountId);
        if (rowsAffected <= 0) {
            throw new ApiException("Failed to close account", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return findById(accountId);
    }

    /**
     * Gets all the accounts for a user id.
     * @param userId The user id to search for.
     * @return A list of account objects owned by the given user id.
     */
    public List<Account> list(Long userId) {
        String query = """
            SELECT *
            FROM accounts
            WHERE user_id = ?
        """;

        return jdbcTemplate.query(query, accountRowMapper, userId);
    }

    /**
     * Deposits the amount into the account.
     * @param accountId The account id to deposit the amount into.
     * @param title The title for the deposit.
     * @param amount The amount to deposit.
     * @return The updated account object.
     */
    public Account deposit(Long accountId, String title, BigDecimal amount) {
        String query = """
            UPDATE accounts
            SET balance = ?
            WHERE id = ?
        """;

        Account account = findById(accountId);
        BigDecimal newBalance = account.balance().add(amount);

        transactionTemplate.execute(status -> {
            try {
                int rowsAffected = jdbcTemplate.update(query, newBalance, accountId);
                if (rowsAffected <= 0) {
                    throw new ApiException(
                            "Failed to deposit the amount", HttpStatus.INTERNAL_SERVER_ERROR
                    );
                }

                transactionRepository.create(accountId, title, amount, newBalance);

                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });

        return findById(accountId);
    }

    /**
     * Withdraws the amount from the account.
     * @param accountId The account id to withdraw the amount from.
     * @param title The title for the withdrawal.
     * @param amount The amount to withdraw.
     * @return The updated account object.
     */
    public Account withdraw(Long accountId, String title, BigDecimal amount) {
        String query = """
            UPDATE accounts
            SET balance = ?
            WHERE id = ?
        """;

        Account account = findById(accountId);
        BigDecimal negativeAmount = amount.multiply(new BigDecimal("-1"));
        BigDecimal newBalance = account.balance().add(negativeAmount);

        transactionTemplate.execute(status -> {
            try {
                int rowsAffected = jdbcTemplate.update(query, newBalance, accountId);
                if (rowsAffected <= 0) {
                    throw new ApiException(
                            "Failed to withdraw the amount", HttpStatus.INTERNAL_SERVER_ERROR
                    );
                }

                transactionRepository.create(accountId, title, negativeAmount, newBalance);

                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });

        return findById(accountId);
    }
}
