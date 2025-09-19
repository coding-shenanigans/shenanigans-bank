package com.codingshenanigans.shenanigans_bank.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserSession {
    private String firstName;
    private String lastName;
    private String email;
    private String accessToken;
    private Integer durationSecs;

    @JsonIgnore
    private String refreshToken;

    public UserSession(
            String firstName,
            String lastName,
            String email,
            String accessToken,
            Integer durationSecs,
            String refreshToken
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accessToken = accessToken;
        this.durationSecs = durationSecs;
        this.refreshToken = refreshToken;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshTokenCookie(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
