package com.myrctc.stationservice.service;

import com.myrctc.stationservice.model.StationCode;
import com.myrctc.stationservice.model.dto.StationDto;
import org.springframework.lang.NonNull;

import java.util.List;

public interface StationService {
    @NonNull
    StationDto createStation(@NonNull final StationDto stationDto);
    @NonNull
    List<StationDto> getMatchingStations(@NonNull final String partialStationCode);
    @NonNull
    StationDto getStation(@NonNull final StationCode stationCode);
    @NonNull
    StationDto updateStation(@NonNull final StationDto stationDto);
}
