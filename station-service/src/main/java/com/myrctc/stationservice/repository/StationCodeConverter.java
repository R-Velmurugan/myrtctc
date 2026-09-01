package com.myrctc.stationservice.repository;

import com.myrctc.stationservice.model.StationCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StationCodeConverter implements AttributeConverter<StationCode, String> {
    @Override
    public String convertToDatabaseColumn(StationCode attribute) {
        return attribute.getStationCode();
    }

    @Override
    public StationCode convertToEntityAttribute(String dbData) {
        return new StationCode(dbData);
    }
}
