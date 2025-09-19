package com.codingshenanigans.shenanigans_bank.dtos;

public class RefreshResponse {
    private UserSession userSession;

    public RefreshResponse(UserSession userSession) {
        this.userSession = userSession;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
