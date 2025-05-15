package com.kantares.store.auth;

import com.kantares.store.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }


    public Jwt generateAccessToken(User user) {
        return generateJwtToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user) {
        return generateJwtToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    public Jwt generateJwtToken(User user, long tokenExpirationTime) {

        Claims claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpirationTime * 1000))
                .build();
        return new Jwt(claims, jwtConfig.getSecretKey());
    }

    public Jwt parseJwt(String token) {
        return Jwt.fromToken(token, jwtConfig.getSecretKey());
    }
}
