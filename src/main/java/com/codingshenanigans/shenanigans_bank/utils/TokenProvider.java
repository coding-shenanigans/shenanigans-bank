package com.codingshenanigans.shenanigans_bank.utils;

import com.codingshenanigans.shenanigans_bank.dtos.AuthToken;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class TokenProvider {
    @Value("${security.access-token.secret}")
    private String accessTokenSecret;
    @Value("${security.access-token.duration-secs}")
    private int accessTokenDurationSecs;
    @Value("${security.refresh-token.secret}")
    private String refreshTokenSecret;
    @Value("${security.refresh-token.duration-secs}")
    private int refreshTokenDurationSecs;
    @Value("${security.refresh-token.cookie.http-only}")
    private boolean refreshTokenCookieHttpOnly;
    @Value("${security.refresh-token.cookie.secure}")
    private boolean refreshTokenCookieSecure;
    @Value("${security.refresh-token.cookie.domain}")
    private String refreshTokenCookieDomain;
    @Value("${security.refresh-token.cookie.path}")
    private String refreshTokenCookiePath;

    /**
     * Generates a new access token.
     * @param userId The user id to issue the token to.
     * @return An auth token object containing the new access token.
     */
    public AuthToken generateAccessToken(long userId) {
        return generateToken(
                userId, accessTokenSecret, accessTokenDurationSecs, Constants.ACCESS_TOKEN_KEY_ID
        );
    }

    /**
     * Generates a new refresh token.
     * @param userId The user id to issue the token to.
     * @return An auth token object containing the new refresh token.
     */
    public AuthToken generateRefreshToken(long userId) {
        return generateToken(
                userId, refreshTokenSecret, refreshTokenDurationSecs, Constants.REFRESH_TOKEN_KEY_ID
        );
    }

    /**
     * Creates a cookie object containing the refresh token.
     * @param refreshToken The refresh token.
     * @return A cookie object containing the refresh token.
     */
    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(Constants.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        refreshTokenCookie.setMaxAge(refreshTokenDurationSecs);
        refreshTokenCookie.setHttpOnly(refreshTokenCookieHttpOnly);
        refreshTokenCookie.setSecure(refreshTokenCookieSecure);
        refreshTokenCookie.setDomain(refreshTokenCookieDomain);
        refreshTokenCookie.setPath(refreshTokenCookiePath);
        return refreshTokenCookie;
    }

    /**
     * Generates a JWT token.
     * @param userId The user id to issue the token to.
     * @param secretKey The secret key used to sign the token.
     * @param durationSecs The token's duration in seconds.
     * @param keyId The secret key's id.
     * @return An auth token object containing the new JWT token.
     */
    private AuthToken generateToken(long userId, String secretKey, int durationSecs, String keyId) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        Instant issuedTime = Instant.now();
        Instant expirationTime = issuedTime.plus(Duration.ofSeconds(durationSecs));

        String refreshToken = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, keyId)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(issuedTime))
                .setExpiration(Date.from(expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return new AuthToken(refreshToken, durationSecs);
    }
}
