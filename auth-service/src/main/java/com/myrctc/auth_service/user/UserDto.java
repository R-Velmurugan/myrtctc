package com.myrctc.auth_service.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {
    Email email;
    String hashedPassword;
    Integer age;
    String name;
    String surname;
}
