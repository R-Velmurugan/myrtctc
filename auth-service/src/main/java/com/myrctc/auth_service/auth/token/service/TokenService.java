package com.myrctc.auth_service.auth.token.service;

import com.myrctc.auth_service.auth.token.Token;
import com.myrctc.auth_service.user.Email;
import com.myrctc.auth_service.user.UserDto;
import org.springframework.lang.NonNull;

public interface TokenService {
    @NonNull
    Token getToken(@NonNull final UserDto user);
    @NonNull
    Token getTokenFromHeader(@NonNull final String authHeader);
    @NonNull
    Email getUserEmail(@NonNull final Token token);
    boolean isTokenExpired(@NonNull final Token token);
    boolean isTokenValid(@NonNull final Token token, @NonNull final UserDto user);
}
