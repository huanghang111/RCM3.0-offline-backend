package com.bosch.rcm.repository;

import com.bosch.rcm.domain.SignalData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Repository
public interface SignalDataRepository extends MongoRepository<SignalData, String> {
}
