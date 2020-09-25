package com.bosch.rcm.repository;

import com.bosch.rcm.domain.Calculations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculationsRepository extends MongoRepository<Calculations, String> {
}
