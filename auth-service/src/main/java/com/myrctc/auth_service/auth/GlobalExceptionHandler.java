package com.myrctc.auth_service.auth;

import com.myrctc.auth_service.user.exception.InvalidEmailException;
import com.myrctc.auth_service.user.exception.UserNameAlreadyInUse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNameAlreadyInUse.class)
    public ResponseEntity<String> handleUserNameAlreadyInUse(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already in use");
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<String> handleInvalidEmail(InvalidEmailException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
