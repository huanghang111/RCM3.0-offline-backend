package com.bosch.rcm.service;

import com.bosch.rcm.domain.*;
import com.bosch.rcm.domain.constant.SignalConstants;
import com.bosch.rcm.repository.SignalDataRepository;
import com.bosch.rcm.repository.SignalErrorRepository;
import com.bosch.rcm.service.dto.SignalCalculatedDTO;
import com.bosch.rcm.service.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SignalDataService {
    public static final String LATEST_DATA = "latestData";
    private final Logger logger = LoggerFactory.getLogger(SignalDataService.class);
    private final SignalService signalService;
    private final DiagnoseService diagnoseService;
    private final SignalDataRepository signalDataRepository;
    private final SignalErrorRepository signalErrorRepository;
    private final CommonUtil commonUtil;
    private final MongoTemplate mongoTemplate;
    private List<SignalCalculatedDTO> listDataCache;
    private final Sort timestampSort = new Sort(Sort.Direction.ASC, "timestamp");

    public SignalDataService(SignalService signalService, DiagnoseService diagnoseService, SignalDataRepository signalDataRepository,
                             SignalErrorRepository signalErrorRepository, CommonUtil commonUtil, MongoTemplate mongoTemplate) {
        this.signalService = signalService;
        this.diagnoseService = diagnoseService;
        this.signalDataRepository = signalDataRepository;
        this.signalErrorRepository = signalErrorRepository;
        this.commonUtil = commonUtil;
        this.mongoTemplate = mongoTemplate;
        this.listDataCache = new ArrayList<>();
    }

    @Cacheable(value = LATEST_DATA)
    public List<SignalCalculatedDTO> getAllLatestData() {
        logger.debug("===================================== Get latest data =====================================");
        List<SignalCalculatedDTO> signalList = signalService.getAllSignalFromCalculations();
        signalList.parallelStream().forEach(signal -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(signal.getSigElementName()))
                .with(timestampSort.descending())
                .limit(1);
            List<SignalData> dataList = mongoTemplate.find(query, SignalData.class);
            if (dataList.size() > 0) {
                signal.setSigElementData(dataList.get(0));
            }
        });
        this.listDataCache = signalList;
        return signalList;
    }

    @CachePut(value = LATEST_DATA)
    public List<SignalCalculatedDTO> updateLatestData(SignalData signalData) {
        listDataCache.forEach(item -> {
            if (item.getSigElementName().equalsIgnoreCase(signalData.getName())) {
                item.setSigElementData(signalData);
            }
        });
        return listDataCache;
    }

    @Caching(evict = {
        @CacheEvict(value = SignalDataService.LATEST_DATA, allEntries = true)
    })
    public void refreshDataCaches() {
        logger.info("Request to refresh data caches");
    }

    @Async
    public void calculatedSignalData(Calculations calculations, List<SignalData> sigOneDataList, List<SignalData> sigTwoDataList,
                                     List<Signal> signalActiveList) {
        if (calculations == null || sigOneDataList == null || sigOneDataList.isEmpty()) {
            return;
        }
        Optional<Signal> sigOwnerOpt = signalActiveList
            .parallelStream()
            .filter(sig -> sig.getName().equalsIgnoreCase(calculations.getNewSignalName()))
            .findFirst();
        if (!sigOwnerOpt.isPresent()) {
            return;
        }
        Signal sigOwner = sigOwnerOpt.get();
        Signal sigOne = calculations.getSignalOne();
        Signal sigTwo = calculations.getSignalTwo();
        if (checkSignalsDeleted(sigOne, sigTwo)) { return; }
        Method methodOne = calculations.getMethodOne();
        Method methodTwo = calculations.getMethodTwo();
        List<SignalData> listData = new ArrayList<>();
        List<SignalError> listError = new ArrayList<>();
        if (sigTwo != null && sigTwoDataList.isEmpty()) {
            // Case signalTwo exists but it doesnt have data
            return;
        }
        for (SignalData sigOneData : sigOneDataList) {
            if (sigOneData == null) {
                continue;
            }
            if (sigTwoDataList.isEmpty()) {
                calculatedData(sigOwner, sigOne, methodOne, methodTwo, listData, listError, sigOneData, null);
            } else {
                for (SignalData sigTwoData : sigTwoDataList) {
                    calculatedData(sigOwner, sigOne, methodOne, methodTwo, listData, listError, sigOneData, sigTwoData);
                }
            }
        }
        listData = signalDataRepository.saveAll(listData);
        listError = signalErrorRepository.saveAll(listError);
        signalService.sendDataToSocket(listData, listError, signalActiveList);
    }

    private void calculatedData(Signal sigOwner, Signal sigOne, Method methodOne, Method methodTwo, List<SignalData> listData,
                                List<SignalError> listError, SignalData sigOneData, SignalData sigTwoData) {
        Double value = null;
        value = diagnoseService.methodResolve(value, sigOne, methodOne, methodTwo, sigOneData, sigTwoData);
        if (value != null) {
            SignalData data = new SignalData();
            data.setName(sigOwner.getName());
            if (sigOwner.getDataType().getName().equalsIgnoreCase(SignalConstants.DATA_TYPE_BOOL)) {
                data.setValues(value.intValue());
            } else {
                data.setValues(value);
            }
            data.setTimestamp(Instant.now());
            listData.add(data);
            Set<Threshold> threshSet = sigOwner.getThresholds();
            if (threshSet != null && threshSet.size() > 0) {
                if (sigOwner.getDataType().getName().equalsIgnoreCase(SignalConstants.DATA_TYPE_BOOL)) {
                    // Boolean signal
                    Threshold threshold = commonUtil.extractThresholdByLevel(threshSet, "");
                    if (threshold != null) {
                        if ((value == 1 && threshold.getValues().equals(SignalConstants.THRESHOLD_VALUE_TRUE))
                            || (value == 0 && threshold.getValues().equals(SignalConstants.THRESHOLD_VALUE_FALSE))) {
                            SignalError error = new SignalError();
                            error.setName(sigOwner.getName());
                            error.setValues(value.intValue());
                            error.setTimestamp(Instant.now());
                            error.setThreshold(threshold);
                            listError.add(error);
                        }
                    }
                } else {
                    // Numeric signal
                    Threshold thresholdMinMin = commonUtil.extractThresholdByLevel(threshSet, SignalConstants.THRESHOLD_MIN_MIN);
                    Threshold thresholdMin = commonUtil.extractThresholdByLevel(threshSet, SignalConstants.THRESHOLD_MIN);
                    Threshold thresholdMax = commonUtil.extractThresholdByLevel(threshSet, SignalConstants.THRESHOLD_MAX);
                    Threshold thresholdMaxMax = commonUtil.extractThresholdByLevel(threshSet, SignalConstants.THRESHOLD_MAX_MAX);
                    // Find out error
                    if (thresholdMinMin != null && thresholdMin != null && thresholdMax != null && thresholdMaxMax != null) {
                        if (value > Double.parseDouble(thresholdMax.getValues()) || value < Double.parseDouble(thresholdMin.getValues())) {
                            SignalError error = new SignalError();
                            // Error at threshold too low
                            if (value < Double.parseDouble(thresholdMinMin.getValues())) {
                                error.setThreshold(thresholdMinMin);
                            }
                            // Error at threshold low
                            else if (value >= Double.parseDouble(thresholdMinMin.getValues()) && value < Double.parseDouble(thresholdMin.getValues())) {
                                error.setThreshold(thresholdMin);
                            }
                            // Error at threshold high
                            else if (value > Double.parseDouble(thresholdMax.getValues()) && value <= Double.parseDouble(thresholdMaxMax.getValues())) {
                                error.setThreshold(thresholdMax);
                            }
                            // Error at threshold too high
                            else {
                                error.setThreshold(thresholdMaxMax);
                            }
                            error.setName(sigOwner.getName());
                            error.setValues(value);
                            error.setTimestamp(sigOneData.getTimestamp());
                            listError.add(error);
                        }
                    }
                }
            }
        }
    }

    private boolean checkSignalsDeleted(Signal sigOne, Signal sigTwo) {
        boolean sigOneDeleted = sigOne.getDeleted();
        boolean sigTwoDeleted = false;
        if (sigTwo != null) {
            sigTwoDeleted = sigTwo.getDeleted();
        }
        // Case signal one or two have been deleted
        return sigOneDeleted || sigTwoDeleted;
    }
}
