package com.bosch.rcm.service;

import com.bosch.rcm.domain.DataType;
import com.bosch.rcm.repository.DataTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTypeService {

    private final DataTypeRepository dataTypeRepository;

    public DataTypeService(DataTypeRepository dataTypeRepository) {
        this.dataTypeRepository = dataTypeRepository;
    }

    public List<DataType> getAllDataType() {
        return dataTypeRepository.findAll();
    }

    public DataType findByName(String name) {
        return dataTypeRepository.findByName(name);
    }
}
