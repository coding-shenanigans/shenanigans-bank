package com.codingshenanigans.shenanigans_bank.services;

import com.codingshenanigans.shenanigans_bank.dtos.AuthToken;
import com.codingshenanigans.shenanigans_bank.dtos.UserSession;
import com.codingshenanigans.shenanigans_bank.exceptions.ApiException;
import com.codingshenanigans.shenanigans_bank.models.User;
import com.codingshenanigans.shenanigans_bank.repositories.SessionRepository;
import com.codingshenanigans.shenanigans_bank.repositories.UserRepository;
import com.codingshenanigans.shenanigans_bank.utils.TokenProvider;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            SessionRepository sessionRepository,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public UserSession signup(String firstName, String lastName, String email, String password) {
        if (userRepository.emailExists(email)) {
            String errorMessage = String.format("The email %s already exists", email);
            throw new ApiException(errorMessage, HttpStatus.CONFLICT);
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = userRepository.create(firstName, lastName, email, hashedPassword);

        AuthToken accessToken = tokenProvider.generateAccessToken(user.id());
        AuthToken refreshToken = tokenProvider.generateRefreshToken(user.id());

        sessionRepository.create(user.id(), refreshToken.getToken());
        Cookie refreshTokenCookie = tokenProvider.createRefreshTokenCookie(refreshToken.getToken());

        return new UserSession(
                user.firstName(),
                user.lastName(),
                user.email(),
                accessToken.getToken(),
                accessToken.getDurationSecs(),
                refreshTokenCookie
        );
    }
}
