package com.codingshenanigans.shenanigans_bank.dtos;

import com.codingshenanigans.shenanigans_bank.models.Account;

public class CloseAccountResponse {
    private Account account;

    public CloseAccountResponse(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
