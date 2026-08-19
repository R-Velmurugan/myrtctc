package com.myrctc.auth_service.auth.token.service;

import com.myrctc.auth_service.auth.token.Token;
import com.myrctc.auth_service.auth.token.TokenConfig;
import com.myrctc.auth_service.user.Email;
import com.myrctc.auth_service.user.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService implements TokenService{
    private final TokenConfig tokenConfig;

    private static final Integer TOKEN_INDEX_IN_HEADER = 7;

    @Override
    @NonNull
    public Token getToken(@NonNull final UserDto user) {
        return new Token(generateToken(user));
    }

    @Override
    @NonNull
    public Token getTokenFromHeader(@NonNull String authHeader) {
        return new Token(authHeader.substring(TOKEN_INDEX_IN_HEADER)); //"Bearer ..." token starts from 7th index
    }

    @NonNull
    private String generateToken(@NonNull final UserDto user){
        final Date now = new Date();
        return Jwts.builder()
                .subject(user.getEmail().email())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenConfig.expirationDuration()))
                .signWith(getSigningKey())
                .compact();
    }

    @NonNull
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(tokenConfig.secret().getBytes(StandardCharsets.UTF_8));
    }

    @NonNull
    @Override
    public Email getUserEmail(@NonNull final Token token) {
        return new Email(extractClaim(token, Claims::getSubject));
    }

    @Override
    public boolean isTokenExpired(@NonNull final Token token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public boolean isTokenValid(@NonNull final Token token, @NonNull final UserDto user) {
        return getUserEmail(token).equals(user.getEmail());
    }

    @NonNull
    private <T> T extractClaim(@NonNull final Token token, @NonNull final Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getClaimsFromToken(token));
    }

    @NonNull
    private Claims getClaimsFromToken(@NonNull final Token token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token.token())
                .getPayload();
    }
}
