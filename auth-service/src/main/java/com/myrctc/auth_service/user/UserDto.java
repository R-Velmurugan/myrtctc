package com.myrctc.auth_service.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
public class UserDto implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return this.getEmail().email();
    }

    Email email;
    String hashedPassword;
    Integer age;
    String name;
    String surname;
}
