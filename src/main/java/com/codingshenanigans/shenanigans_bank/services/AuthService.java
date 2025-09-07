package com.codingshenanigans.shenanigans_bank.services;

import com.codingshenanigans.shenanigans_bank.exceptions.ResourceConflictException;
import com.codingshenanigans.shenanigans_bank.models.Session;
import com.codingshenanigans.shenanigans_bank.models.User;
import com.codingshenanigans.shenanigans_bank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Session signup(String firstName, String lastName, String email, String password) {
        if (userRepository.emailExists(email)) {
            String errorMessage = String.format("The email %s already exists", email);
            throw new ResourceConflictException(errorMessage);
        }

        // TODO: hash password
        // TODO: add new user to database
        User user = new User(firstName, lastName, email, password);
        userRepository.createUser(user);
        // TODO: generate access and refresh tokens
        // TODO: return session object

        return null;
    }
}
