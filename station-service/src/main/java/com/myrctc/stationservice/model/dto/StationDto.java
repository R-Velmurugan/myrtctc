package com.myrctc.stationservice.model.dto;

import com.myrctc.stationservice.model.StationCode;
import lombok.Builder;

@Builder
public record StationDto(StationCode stationCode, String stationName) {
}
