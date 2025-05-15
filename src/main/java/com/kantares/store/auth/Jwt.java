package com.kantares.store.auth;

import com.kantares.store.user.Role;
import com.kantares.store.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.util.Date;

@AllArgsConstructor
@Getter
public class Jwt {
    @NotNull
    private final Claims claims;

    @NotNull
    private final SecretKey key;

    public boolean isValid() {
        Date expiration = claims.getExpiration();
        return expiration.after(new Date());
    }

    public boolean isExpired() {
        Date expiration = claims.getExpiration();
        return expiration == null || expiration.before(new Date());
    }

    public String getSubject() {
        return claims.getSubject();
    }

    public Long getUserId() {
        return Long.valueOf(getSubject());
    }

    public Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    @Override
    public String toString() {
        return Jwts.builder().claims(claims).signWith(key).compact();
    }

    public static Jwt fromToken(String token, SecretKey secretKey) {
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
        Claims claims = parser.parseSignedClaims(token).getPayload();

        return new Jwt(claims, secretKey);
    }
}
