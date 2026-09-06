package com.myrctc.stationservice.controller;

import com.myrctc.stationservice.exception.InvalidStationCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidStationCodeException.class)
    public ResponseEntity<String> handleInvalidStationCode(InvalidStationCodeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The status code provided is invalid. Station code should contain min 3 and max 4 letters only.");
    }
}
