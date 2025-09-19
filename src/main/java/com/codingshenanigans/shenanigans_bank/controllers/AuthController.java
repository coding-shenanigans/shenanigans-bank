package com.codingshenanigans.shenanigans_bank.controllers;

import com.codingshenanigans.shenanigans_bank.dtos.*;
import com.codingshenanigans.shenanigans_bank.services.AuthService;
import com.codingshenanigans.shenanigans_bank.utils.Constants;
import com.codingshenanigans.shenanigans_bank.utils.TokenProvider;
import jakarta.servlet.http.Cookie;
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
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthService authService, TokenProvider tokenProvider) {
        this.authService = authService;
        this.tokenProvider = tokenProvider;
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
        Cookie refreshTokenCookie = tokenProvider.createRefreshTokenCookie(
                userSession.getRefreshToken()
        );

        servletResponse.addCookie(refreshTokenCookie);
        SignupResponse response = new SignupResponse(userSession);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(
            @Valid @RequestBody SigninRequest request, HttpServletResponse servletResponse
    ) {
        UserSession userSession = authService.signin(request.getEmail(), request.getPassword());
        Cookie refreshTokenCookie = tokenProvider.createRefreshTokenCookie(
                userSession.getRefreshToken()
        );

        servletResponse.addCookie(refreshTokenCookie);
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
        Cookie refreshTokenCookie = tokenProvider.createRefreshTokenCookie(
                userSession.getRefreshToken()
        );

        servletResponse.addCookie(refreshTokenCookie);
        RefreshResponse response = new RefreshResponse(userSession);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signout(
            @CookieValue(name = Constants.REFRESH_TOKEN_COOKIE_NAME, required = false)
            String refreshToken,
            HttpServletResponse servletResponse
    ) {
        authService.signout(refreshToken);
        Cookie refreshTokenCookie = tokenProvider.createRefreshTokenCookie("");
        refreshTokenCookie.setMaxAge(0);

        servletResponse.addCookie(refreshTokenCookie);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
