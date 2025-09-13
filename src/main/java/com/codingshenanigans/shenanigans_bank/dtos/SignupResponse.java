package com.codingshenanigans.shenanigans_bank.dtos;

public class SignupResponse {
    private UserSession userSession;

    public SignupResponse(UserSession userSession) {
        this.userSession = userSession;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
