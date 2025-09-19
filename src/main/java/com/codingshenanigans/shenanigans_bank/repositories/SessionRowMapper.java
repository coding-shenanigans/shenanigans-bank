package com.codingshenanigans.shenanigans_bank.repositories;

import com.codingshenanigans.shenanigans_bank.models.Session;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

@Component
public class SessionRowMapper implements RowMapper<Session> {
    private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Override
    public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Session(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("refresh_token"),
                rs.getTimestamp("created_at", UTC_CALENDAR).toInstant(),
                rs.getTimestamp("updated_at", UTC_CALENDAR).toInstant()
        );
    }
}
