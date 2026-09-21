package com.myrctc.stationservice.redis;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Searchable;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Builder
@Getter
@Document("station")
public class StationDocument {
    @Id // tag indexing
    private String stationCode;
    @Searchable //substring indexing
    private String stationName;
}
