package com.myrctc.auth_service.user.exception;

public class UserNameAlreadyInUse extends RuntimeException{
    public UserNameAlreadyInUse(String message){
        super(message);
    }
}
