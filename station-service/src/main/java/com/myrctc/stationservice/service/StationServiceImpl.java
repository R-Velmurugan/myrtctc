package com.myrctc.stationservice.service;

import com.myrctc.stationservice.entity.Station;
import com.myrctc.stationservice.exception.InvalidStationCodeException;
import com.myrctc.stationservice.model.StationCode;
import com.myrctc.stationservice.model.dto.StationDto;
import com.myrctc.stationservice.redis.StationDocument;
import com.myrctc.stationservice.redis.StationNameRepository;
import com.myrctc.stationservice.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final StationNameRepository stationNameRepository;

    @Override
    @NonNull
    public StationDto createStation(@NonNull final StationDto stationDto) {
        stationRepository.save(convertStationDtoToEntity(stationDto));
        return stationDto;
    }

    @Override
    @NonNull
    public StationDto getStation(@NonNull final StationCode stationCode) {
        return stationRepository.findById(stationCode.getStationCode())
                .map(this::convertStationEntityToDto)
                .orElseThrow(() -> new InvalidStationCodeException(stationCode.getStationCode()));
    }

    @Override
    @NonNull
    public List<StationDto> getStation(@NonNull final String stationName) {
        final String lowerCaseName = stationName.trim().toLowerCase();
        final String searchQuery = buildRedisSearchQuery(lowerCaseName);

        return stationNameRepository.searchByFuzzyAndInfix(String.format("@stationName:%s", searchQuery))
                .stream()
                .map(this::convertStationDocumentToDto)
                .toList();
    }

    @NonNull
    private String buildRedisSearchQuery(@NonNull final String term) {
        if (term.length() < 4) {
            return String.format("*%s*", term);
        }
        return String.format("(*%1$s* | %%%1$s%%)", term);
    }

    @Override
    @NonNull
    public StationDto updateStation(@NonNull final StationDto stationDto) {
        Station station = stationRepository.findById(stationDto.stationCode().getStationCode())
                .orElseThrow(() -> new InvalidStationCodeException(stationDto.stationCode().getStationCode()));
        station = updateStationUsingStationDto(stationDto, station);

        return convertStationEntityToDto(stationRepository.save(station));
    }

    @Override
    public boolean deleteStation(@NonNull final StationCode stationCode) {
        stationRepository.deleteById(stationCode.getStationCode());
        return !stationRepository.existsById(stationCode.getStationCode());
    }

    @NonNull
    private StationDto convertStationDocumentToDto(@NonNull final StationDocument stationDocument) {
        return StationDto.builder()
                .stationCode(StationCode.of(stationDocument.getStationCode()))
                .stationName(stationDocument.getStationName())
                .build();
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

    @NonNull
    private Station updateStationUsingStationDto(@NonNull final StationDto station, @NonNull final Station stationEntity) {
        return Station.builder()
                .stationCode(stationEntity.getStationCode())
                .stationName(station.stationName())
                .build();
    }
}
