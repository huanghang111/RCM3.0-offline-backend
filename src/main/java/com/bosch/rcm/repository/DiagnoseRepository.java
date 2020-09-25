package com.bosch.rcm.repository;

import com.bosch.rcm.domain.Diagnose;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnoseRepository extends MongoRepository<Diagnose, String> {
    //region check exist
    boolean existsById(String id);
    //endregion

}
