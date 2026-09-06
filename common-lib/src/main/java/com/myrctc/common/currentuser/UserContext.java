package com.myrctc.common.currentuser;

public record UserContext(String email) { //TODO: add regex here or migrate email validation to a common repo
    public static UserContext withEmail(String email) {
        return new UserContext(email);
    }
}
