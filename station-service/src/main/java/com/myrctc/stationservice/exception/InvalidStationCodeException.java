package com.myrctc.stationservice.exception;

import org.springframework.lang.NonNull;

public class InvalidStationCodeException extends RuntimeException {
    public InvalidStationCodeException(@NonNull final String invalidStatusCode) {
        super(String.format("The status code provided - %s is invalid. Station code should contain min 3 and max 4 letters only.", invalidStatusCode));
    }
}
