package com.bosch.rcm.repository;

import com.bosch.rcm.domain.CacheData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheDataRepository extends MongoRepository<CacheData, String> {
}
