package com.myrctc.auth_service.user.service;

import com.myrctc.auth_service.user.Email;
import com.myrctc.auth_service.user.UserDto;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    @NonNull
    Optional<UserDto> getUserByEmail(@NonNull final Email email);
    @NonNull
    UserDto registerUser(@NonNull final UserDto userDto);
}
