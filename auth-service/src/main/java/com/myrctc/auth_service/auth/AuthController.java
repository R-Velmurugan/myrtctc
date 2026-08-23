package com.myrctc.auth_service.auth;

import com.myrctc.auth_service.auth.token.JwtResponse;
import com.myrctc.auth_service.auth.token.Token;
import com.myrctc.auth_service.auth.token.service.TokenService;
import com.myrctc.auth_service.user.LoginRequest;
import com.myrctc.auth_service.user.UserDto;
import com.myrctc.auth_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@NonNull final @RequestBody UserDto userDto){
        return userService.registerUser(userDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@NonNull final @RequestBody LoginRequest loginRequest){
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDto user = (UserDto) authentication.getPrincipal();
        final Token token = tokenService.getToken(user);
        return new JwtResponse(token);
    }
}