package com.myrctc.stationservice.controller;

import com.myrctc.stationservice.model.dto.StationDto;
import com.myrctc.stationservice.service.StationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    @PostMapping("/station")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<StationDto> createStation (@NonNull final @RequestBody StationDto stationDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stationService.createStation(stationDto));
    }
}
