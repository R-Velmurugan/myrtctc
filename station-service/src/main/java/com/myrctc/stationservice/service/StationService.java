package com.myrctc.stationservice.service;

import com.myrctc.stationservice.model.dto.StationDto;
import org.springframework.lang.NonNull;

import java.util.List;

public interface StationService {
    @NonNull
    StationDto createStation(@NonNull final StationDto stationDto);

    List<StationDto> getMatchingStations(@NonNull final String partialStationCode);
}
