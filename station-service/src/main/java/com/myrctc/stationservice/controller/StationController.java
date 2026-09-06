package com.myrctc.stationservice.controller;

import com.myrctc.stationservice.model.dto.StationDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StationController {

    @PostMapping("/station")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<StationDto> createStation (@NonNull final @RequestBody StationDto stationDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stationDto);
    }
}
