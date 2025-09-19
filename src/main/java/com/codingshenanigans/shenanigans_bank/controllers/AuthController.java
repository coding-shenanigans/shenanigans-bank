package com.codingshenanigans.shenanigans_bank.controllers;

import com.codingshenanigans.shenanigans_bank.dtos.*;
import com.codingshenanigans.shenanigans_bank.services.AuthService;
import com.codingshenanigans.shenanigans_bank.utils.Constants;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
            @Valid @RequestBody SignupRequest request, HttpServletResponse servletResponse
    ) {
        UserSession userSession = authService.signup(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword()
        );

        servletResponse.addCookie(userSession.getRefreshTokenCookie());
        SignupResponse response = new SignupResponse(userSession);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(
            @Valid @RequestBody SigninRequest request, HttpServletResponse servletResponse
    ) {
        UserSession userSession = authService.signin(request.getEmail(), request.getPassword());

        servletResponse.addCookie(userSession.getRefreshTokenCookie());
        SigninResponse response = new SigninResponse(userSession);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @CookieValue(name = Constants.REFRESH_TOKEN_COOKIE_NAME, required = false)
            String refreshToken,
            HttpServletResponse servletResponse
    ) {
        UserSession userSession = authService.refresh(refreshToken);

        servletResponse.addCookie(userSession.getRefreshTokenCookie());
        RefreshResponse response = new RefreshResponse(userSession);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
