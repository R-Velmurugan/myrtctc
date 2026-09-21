package com.myrctc.stationservice.redis;

import com.redis.om.spring.annotations.Query;
import com.redis.om.spring.repository.RedisDocumentRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;

//No need of Repo annotation as spring automatically recognizes this since it is extending a Repo.
public interface StationNameRepository extends RedisDocumentRepository<StationDocument, String> {
    @Query("$query")
    List<StationDocument> searchByFuzzyAndInfix(@NonNull final @Param("query") String query);
}
