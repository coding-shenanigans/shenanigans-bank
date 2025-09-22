package com.codingshenanigans.shenanigans_bank.controllers;

import com.codingshenanigans.shenanigans_bank.dtos.*;
import com.codingshenanigans.shenanigans_bank.models.Account;
import com.codingshenanigans.shenanigans_bank.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/open")
    public ResponseEntity<OpenAccountResponse> open(
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader,
            @Valid @RequestBody OpenAccountRequest request
    ) {
        Account account = accountService.open(
                authorizationHeader, request.getBalance(), request.getType()
        );

        OpenAccountResponse response = new OpenAccountResponse(account);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{accountId}/close")
    public ResponseEntity<CloseAccountResponse> close(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long accountId
            ) {
        Account account = accountService.close(authorizationHeader, accountId);

        CloseAccountResponse response = new CloseAccountResponse(account);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ListAccountsResponse> listAccounts(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        List<Account> accounts = accountService.listAccounts(authorizationHeader);

        ListAccountsResponse response = new ListAccountsResponse(accounts);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<GetAccountResponse> getAccount(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long accountId
    ) {
        Account account = accountService.getAccount(authorizationHeader, accountId);

        GetAccountResponse response = new GetAccountResponse(account);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
