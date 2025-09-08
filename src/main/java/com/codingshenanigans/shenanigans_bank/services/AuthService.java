package com.codingshenanigans.shenanigans_bank.services;

import com.codingshenanigans.shenanigans_bank.exceptions.ApiException;
import com.codingshenanigans.shenanigans_bank.models.Session;
import com.codingshenanigans.shenanigans_bank.models.User;
import com.codingshenanigans.shenanigans_bank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Session signup(String firstName, String lastName, String email, String password) {
        if (userRepository.emailExists(email)) {
            String errorMessage = String.format("The email %s already exists", email);
            throw new ApiException(errorMessage, HttpStatus.CONFLICT);
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = userRepository.create(firstName, lastName, email, hashedPassword);

        // TODO: generate access and refresh tokens

        return new Session(user, "", "");
    }
}
