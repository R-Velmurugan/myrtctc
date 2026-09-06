package com.myrctc.stationservice.repository;

import com.myrctc.stationservice.entity.Station;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
    List<Station> findByStationCodeContainingIgnoreCase(@NonNull final String partialStationCode);
}
