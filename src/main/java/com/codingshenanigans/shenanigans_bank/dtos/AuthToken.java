package com.codingshenanigans.shenanigans_bank.dtos;

public class AuthToken {
    private String token;
    private Integer durationSecs;

    public AuthToken(String token, Integer durationSecs) {
        this.token = token;
        this.durationSecs = durationSecs;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDurationSecs() {
        return durationSecs;
    }

    public void setDurationSecs(Integer durationSecs) {
        this.durationSecs = durationSecs;
    }
}
