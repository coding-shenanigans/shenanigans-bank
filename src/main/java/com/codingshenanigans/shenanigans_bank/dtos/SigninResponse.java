package com.codingshenanigans.shenanigans_bank.dtos;

public class SigninResponse {
    private UserSession userSession;

    public SigninResponse(UserSession userSession) {
        this.userSession = userSession;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
