package com.codingshenanigans.shenanigans_bank.services;

import com.codingshenanigans.shenanigans_bank.exceptions.ApiException;
import com.codingshenanigans.shenanigans_bank.models.Account;
import com.codingshenanigans.shenanigans_bank.models.AccountStatus;
import com.codingshenanigans.shenanigans_bank.models.AccountType;
import com.codingshenanigans.shenanigans_bank.repositories.AccountRepository;
import com.codingshenanigans.shenanigans_bank.utils.TokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    public AccountService(AccountRepository accountRepository, TokenProvider tokenProvider) {
        this.accountRepository = accountRepository;
        this.tokenProvider = tokenProvider;
    }

    public Account open(String authorizationHeader, BigDecimal balance, AccountType type) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ApiException("Invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String accessToken = authorizationHeader.substring(7);
        Claims claims = tokenProvider.validateAccessToken(accessToken);
        if (claims == null) {
            throw new ApiException("Invalid access token", HttpStatus.UNAUTHORIZED);
        }

        try {
            long userId = Long.parseLong(claims.getSubject());
            return accountRepository.create(userId, balance, type, AccountStatus.OPEN);
        } catch (NumberFormatException e) {
            throw new ApiException(
                    "Failed to parse subject claim", HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public List<Account> listAccounts(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ApiException("Invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String accessToken = authorizationHeader.substring(7);
        Claims claims = tokenProvider.validateAccessToken(accessToken);
        if (claims == null) {
            throw new ApiException("Invalid access token", HttpStatus.UNAUTHORIZED);
        }

        try {
            long userId = Long.parseLong(claims.getSubject());
            return accountRepository.list(userId);
        } catch (NumberFormatException e) {
            throw new ApiException(
                    "Failed to parse subject claim", HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
