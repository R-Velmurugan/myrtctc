package com.myrctc.auth_service.auth;

import com.myrctc.auth_service.user.UserDto;
import com.myrctc.auth_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@NonNull final @RequestBody UserDto userDto){
        return userService.registerUser(userDto);
    }
}