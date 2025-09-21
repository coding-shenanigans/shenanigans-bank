package com.codingshenanigans.shenanigans_bank.repositories;

import com.codingshenanigans.shenanigans_bank.models.Account;
import com.codingshenanigans.shenanigans_bank.models.AccountStatus;
import com.codingshenanigans.shenanigans_bank.models.AccountType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

@Component
public class AccountRowMapper implements RowMapper<Account> {
    private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Account(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getBigDecimal("balance"),
                AccountType.valueOf(rs.getString("type")),
                AccountStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("created_at", UTC_CALENDAR).toInstant(),
                rs.getTimestamp("updated_at", UTC_CALENDAR).toInstant()
        );
    }
}
