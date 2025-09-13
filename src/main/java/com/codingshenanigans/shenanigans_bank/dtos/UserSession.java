package com.codingshenanigans.shenanigans_bank.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.Cookie;

public class UserSession {
    private String firstName;
    private String lastName;
    private String email;
    private String accessToken;
    private Integer durationSecs;

    @JsonIgnore
    private Cookie refreshTokenCookie;

    public UserSession(
            String firstName,
            String lastName,
            String email,
            String accessToken,
            Integer durationSecs,
            Cookie refreshTokenCookie
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accessToken = accessToken;
        this.durationSecs = durationSecs;
        this.refreshTokenCookie = refreshTokenCookie;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getDurationSecs() {
        return durationSecs;
    }

    public void setDurationSecs(Integer durationSecs) {
        this.durationSecs = durationSecs;
    }

    public Cookie getRefreshTokenCookie() {
        return refreshTokenCookie;
    }

    public void setRefreshTokenCookie(Cookie refreshTokenCookie) {
        this.refreshTokenCookie = refreshTokenCookie;
    }
}
