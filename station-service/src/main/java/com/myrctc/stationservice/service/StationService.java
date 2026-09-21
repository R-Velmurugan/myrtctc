package com.myrctc.stationservice.service;

import com.myrctc.stationservice.model.StationCode;
import com.myrctc.stationservice.model.dto.StationDto;
import org.springframework.lang.NonNull;

import java.util.List;

public interface StationService {
    @NonNull
    StationDto createStation(@NonNull final StationDto stationDto);
    @NonNull
    StationDto getStation(@NonNull final StationCode stationCode);
    @NonNull
    List<StationDto> getStation(@NonNull final String stationName);
    @NonNull
    StationDto updateStation(@NonNull final StationDto stationDto);
    boolean deleteStation(@NonNull final StationCode stationCode);
}
