package com.myrctc.stationservice.service;

import com.myrctc.stationservice.model.dto.StationDto;
import org.springframework.lang.NonNull;

public interface StationService {
    @NonNull
    StationDto createStation (@NonNull final StationDto stationDto);
}
