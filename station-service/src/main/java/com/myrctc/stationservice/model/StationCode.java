package com.myrctc.stationservice.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.myrctc.stationservice.exception.InvalidStationCodeException;
import org.springframework.lang.NonNull;

import java.util.Objects;

public record StationCode(@JsonValue String stationCode) {
    public String getStationCode() {
        return stationCode;
    }
    public static StationCode of(@NonNull final String stationCode) {
        return new StationCode(stationCode);
    }
    public StationCode {
        Objects.requireNonNull(stationCode);
        stationCode = stationCode.toUpperCase();

        if(!stationCode.matches("^[A-Z]{2,4}$")) throw new InvalidStationCodeException(stationCode);
    }
}
