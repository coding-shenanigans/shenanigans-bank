package com.codingshenanigans.shenanigans_bank.repositories;

import com.codingshenanigans.shenanigans_bank.models.Transaction;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

@Component
public class TransactionRowMapper implements RowMapper<Transaction> {
    private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Transaction(
                rs.getLong("id"),
                rs.getLong("account_id"),
                rs.getString("title"),
                rs.getBigDecimal("amount"),
                rs.getBigDecimal("new_balance"),
                rs.getTimestamp("created_at", UTC_CALENDAR).toInstant(),
                rs.getTimestamp("updated_at", UTC_CALENDAR).toInstant()
        );
    }
}
