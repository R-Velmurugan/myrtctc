package com.myrctc.auth_service.auth.token.service;

import com.myrctc.auth_service.auth.token.Token;
import com.myrctc.auth_service.auth.token.TokenConfig;
import com.myrctc.auth_service.user.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService implements TokenService{
    private final TokenConfig tokenConfig;

    @Override
    public Token getToken(@NonNull final UserDto user) {
        return new Token(generateToken(user));
    }

    private String generateToken(@NonNull final UserDto user){
        final Date now = new Date();
        return Jwts.builder()
                .subject(user.getEmail().email())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenConfig.expirationDuration()))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(tokenConfig.secret().getBytes(StandardCharsets.UTF_8));
    }
}
