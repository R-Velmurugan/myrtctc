package com.myrctc.stationservice.service;

import com.myrctc.stationservice.entity.Station;
import com.myrctc.stationservice.exception.InvalidSearch;
import com.myrctc.stationservice.model.StationCode;
import com.myrctc.stationservice.model.dto.StationDto;
import com.myrctc.stationservice.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    @Override
    public @NonNull StationDto createStation(@NonNull final StationDto stationDto) {
        stationRepository.save(convertStationDtoToEntity(stationDto));
        return stationDto;
    }

    @Override
    public List<StationDto> getMatchingStations(@NonNull final String partialStationCode) {
        if(partialStationCode.chars().anyMatch(Character::isDigit)) throw new InvalidSearch(partialStationCode);
        return stationRepository.findByStationCodeContainingIgnoreCase(partialStationCode).stream()
                .map(this::convertStationEntityToDto)
                .toList();
    }

    @NonNull
    private Station convertStationDtoToEntity(@NonNull final StationDto stationDto) {
        return Station.builder()
                .stationCode(stationDto.stationCode().getStationCode())
                .stationName(stationDto.stationName())
                .build();
    }

    @NonNull
    private StationDto convertStationEntityToDto(@NonNull final Station station) {
        return StationDto.builder()
                .stationCode(new StationCode(station.getStationCode()))
                .stationName(station.getStationName())
                .build();
    }
}
