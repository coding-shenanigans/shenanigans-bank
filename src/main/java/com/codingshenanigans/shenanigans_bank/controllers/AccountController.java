package com.codingshenanigans.shenanigans_bank.controllers;

import com.codingshenanigans.shenanigans_bank.dtos.OpenAccountRequest;
import com.codingshenanigans.shenanigans_bank.dtos.OpenAccountResponse;
import com.codingshenanigans.shenanigans_bank.models.Account;
import com.codingshenanigans.shenanigans_bank.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
