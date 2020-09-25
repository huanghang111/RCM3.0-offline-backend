package com.bosch.rcm.repository;

import com.bosch.rcm.domain.DataType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataTypeRepository extends MongoRepository<DataType, String> {

    DataType findByName(String name);
}
