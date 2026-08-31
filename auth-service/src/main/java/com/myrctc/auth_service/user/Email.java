package com.myrctc.auth_service.user;

import com.fasterxml.jackson.annotation.JsonValue;
import com.myrctc.auth_service.user.exception.InvalidEmailException;
import io.micrometer.common.util.StringUtils;

import java.io.Serializable;
import java.util.regex.Pattern;

public record Email(@JsonValue String email) implements Serializable {
    private static final Pattern emailRegex = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    public Email{
        if(StringUtils.isBlank(email) || !emailRegex.matcher(email.trim()).matches()){
            throw new InvalidEmailException("Invalid email address");
        }
        email = email.trim().toLowerCase();
    }
}
