package com.myrctc.stationservice.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.myrctc.stationservice.exception.InvalidStationCodeException;

import java.util.Objects;

public record StationCode(@JsonValue String stationCode) {
    public String getStationCode() {
        return stationCode;
    }
    public StationCode {
        Objects.requireNonNull(stationCode);
        stationCode = stationCode.toUpperCase();

        if(!stationCode.matches("^[A-Z]{3,4}$")) throw new InvalidStationCodeException(stationCode);
    }
}
