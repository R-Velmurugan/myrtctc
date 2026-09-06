package com.myrctc.stationservice.exception;

import org.springframework.lang.NonNull;

public class InvalidSearch extends RuntimeException {
    public InvalidSearch(@NonNull final String searchTerm) {
        super(String.format("%s is not a valid searchable station code", searchTerm));
    }
}
