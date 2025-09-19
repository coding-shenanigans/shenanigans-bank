package com.codingshenanigans.shenanigans_bank.services;

import com.codingshenanigans.shenanigans_bank.dtos.AuthToken;
import com.codingshenanigans.shenanigans_bank.dtos.UserSession;
import com.codingshenanigans.shenanigans_bank.exceptions.ApiException;
import com.codingshenanigans.shenanigans_bank.models.Session;
import com.codingshenanigans.shenanigans_bank.models.User;
import com.codingshenanigans.shenanigans_bank.repositories.SessionRepository;
import com.codingshenanigans.shenanigans_bank.repositories.UserRepository;
import com.codingshenanigans.shenanigans_bank.utils.TokenProvider;
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
        if (userRepository.findByEmail(email) != null) {
            String errorMessage = String.format("The email %s already exists", email);
            throw new ApiException(errorMessage, HttpStatus.CONFLICT);
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = userRepository.create(firstName, lastName, email, hashedPassword);

        AuthToken accessToken = tokenProvider.generateAccessToken(user.id());
        AuthToken refreshToken = tokenProvider.generateRefreshToken(user.id());

        sessionRepository.create(user.id(), refreshToken.getToken());

        return new UserSession(
                user.firstName(),
                user.lastName(),
                user.email(),
                accessToken.getToken(),
                accessToken.getDurationSecs(),
                refreshToken.getToken()
        );
    }

    public UserSession signin(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null || !passwordEncoder.matches(password, user.password())) {
            throw new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        AuthToken accessToken = tokenProvider.generateAccessToken(user.id());
        AuthToken refreshToken = tokenProvider.generateRefreshToken(user.id());

        sessionRepository.create(user.id(), refreshToken.getToken());

        return new UserSession(
                user.firstName(),
                user.lastName(),
                user.email(),
                accessToken.getToken(),
                accessToken.getDurationSecs(),
                refreshToken.getToken()
        );
    }

    public UserSession refresh(String refreshToken) {
        if (tokenProvider.validateRefreshToken(refreshToken) == null) {
            throw new ApiException("The refresh token is not valid", HttpStatus.UNAUTHORIZED);
        }

        Session session = sessionRepository.findByRefreshToken(refreshToken);
        if (session == null) {
            throw new ApiException("The session was not found", HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findById(session.userId());
        if (user == null) {
            throw new ApiException("The session's owner was not found", HttpStatus.NOT_FOUND);
        }

        AuthToken accessToken = tokenProvider.generateAccessToken(session.userId());
        AuthToken newRefreshToken = tokenProvider.generateRefreshToken(session.userId());

        sessionRepository.updateRefreshToken(session.id(), newRefreshToken.getToken());

        return new UserSession(
                user.firstName(),
                user.lastName(),
                user.email(),
                accessToken.getToken(),
                accessToken.getDurationSecs(),
                newRefreshToken.getToken()
        );
    }

    public void signout(String refreshToken) {
        Session session = sessionRepository.findByRefreshToken(refreshToken);
        if (session == null) {
            throw new ApiException("The session was not found", HttpStatus.NOT_FOUND);
        }

        sessionRepository.delete(session.id());
    }
}
