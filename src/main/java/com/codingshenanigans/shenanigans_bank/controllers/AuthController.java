package com.codingshenanigans.shenanigans_bank.controllers;

import com.codingshenanigans.shenanigans_bank.dtos.SignupRequest;
import com.codingshenanigans.shenanigans_bank.dtos.SignupResponse;
import com.codingshenanigans.shenanigans_bank.models.Session;
import com.codingshenanigans.shenanigans_bank.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        Session session = authService.signup(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword()
        );

        // TODO: add cookie with refresh token

        SignupResponse response = new SignupResponse(session);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
