package com.bosch.rcm.repository;

import com.bosch.rcm.domain.Signal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignalRepository extends MongoRepository<Signal, String> {
    String PRJ_SIGNAL_POINT = "{'id' : 1, 'signalId' : 1, 'name' : 1, 'descriptions' : 1, 'dataType' : '1', 'unit' : 1, " +
        "'digit' : 1, 'thresholds' : 1, 'displayTrue' : 1, 'displayFalse' : 1, 'cmd': 1}";

    @Query(fields = "{'name' : 1, 'thresholds' : 1, 'dataType' : 1, 'displayTrue' : 1, 'displayFalse' : 1, 'digit' : 1, 'project': 1}")
    List<Signal> findAllByIsDeletedFalse();

    @Query(fields = "{'name' : 1, 'thresholds' : 1, 'dataType' : 1, 'calculations' : 1}")
    List<Signal> findAllBySignalTypeAndIsDeletedFalse(String signalType);

    @Query(fields = "{'name' : 1, 'thresholds' : 1, 'dataType' : 1, 'calculations' : 1}")
    List<Signal> findAllByCalculationsNotNullAndIsDeletedFalseAndSignalType(String signalType);

    @Query(fields = "{'name' : 1, 'triggerType' : 1, 'triggerHour' : 1, 'triggerMinute' : 1, 'triggerInterval' : 1, " +
        "'plcId' : 1, 'plcOrder' : 1, 'cmd' : 1}")
    List<Signal> findAllByDataType_NameAndTriggerTypeAndIsDeletedFalse(String dataType, String triggerType);

    Signal findByNameAndIsDeletedFalse(String name);

    @Query(fields = PRJ_SIGNAL_POINT)
    List<Signal> findAllByDataType_NameNotInAndIsDeletedFalse(List<String> dataType);
}
