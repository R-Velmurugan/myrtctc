package com.myrctc.auth_service.user.service;

import com.myrctc.auth_service.user.Email;
import com.myrctc.auth_service.user.UserDto;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserService {
    @NonNull
    Optional<UserDto> getUserByEmail(@NonNull final Email email);
    @NonNull
    UserDto registerUser(@NonNull final UserDto userDto);
}
