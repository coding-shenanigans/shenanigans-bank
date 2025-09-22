package com.codingshenanigans.shenanigans_bank.dtos;

import com.codingshenanigans.shenanigans_bank.models.Transaction;

import java.util.List;

public class ListTransactionsResponse {
    List<Transaction> transactions;

    public ListTransactionsResponse(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
