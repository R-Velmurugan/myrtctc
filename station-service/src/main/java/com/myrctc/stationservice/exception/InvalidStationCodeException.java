package com.myrctc.stationservice.exception;

public class InvalidStationCodeException extends RuntimeException {
    public InvalidStationCodeException(String message) {
        super(message);
    }
}
