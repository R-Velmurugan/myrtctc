package com.myrctc.auth_service.auth.token.service;

import com.myrctc.auth_service.auth.token.Token;
import com.myrctc.auth_service.user.UserDto;
import org.springframework.lang.NonNull;

public interface TokenService {
    Token getToken(@NonNull final UserDto user);
}
