package com.bosch.rcm.service;

import com.bosch.rcm.domain.Calculations;
import com.bosch.rcm.domain.Signal;
import com.bosch.rcm.domain.SignalData;
import com.bosch.rcm.domain.SignalError;
import com.bosch.rcm.domain.constant.SignalConstants;
import com.bosch.rcm.domain.dto.ErrorDataDTO;
import com.bosch.rcm.repository.CalculationsRepository;
import com.bosch.rcm.repository.SignalRepository;
import com.bosch.rcm.service.dto.SignalCalculatedDTO;
import com.bosch.rcm.service.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SignalService {
    public static final String INTENSIVE_LIST = "intensiveList";
    public static final String ALL_SIGNAL_ACTIVE = "allSignalActive";
    public static final String CALCULATED_SIGNALS = "calculatedSignals";
    private final Logger logger = LoggerFactory.getLogger(SignalService.class);
    private final SignalRepository signalRepository;
    private final CalculationsRepository calculationsRepository;
    private final CommonUtil commonUtil;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public SignalService(SignalRepository signalRepository, CalculationsRepository calculationsRepository, CommonUtil commonUtil,
                         SimpMessagingTemplate simpMessagingTemplate) {
        this.signalRepository = signalRepository;
        this.calculationsRepository = calculationsRepository;
        this.commonUtil = commonUtil;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    // Get all signal active in system to consume data from mq
    @Cacheable(value = ALL_SIGNAL_ACTIVE)
    public List<Signal> getAllSignalActive() {
        logger.debug("============================== Get all signals active ==============================");
        return signalRepository.findAllByIsDeletedFalse();
    }

    // Only needs for intensive signals list
    @Cacheable(value = INTENSIVE_LIST)
    public List<Signal> getListIntensiveSignals() {
        logger.debug("============================== Get all intensive signals ==============================");
        List<Signal> filterSignal = new ArrayList<>();
        List<Signal> signalList = signalRepository.findAllByDataType_NameAndTriggerTypeAndIsDeletedFalse(SignalConstants.DATA_TYPE_INTENSIVE,
            SignalConstants.TRIGGER_METHOD_AUTO);
        int plc = -1, cmd = -1;
        for (Signal sig : signalList) {
            if (sig.getPlcId() != plc && sig.getCmd() != cmd) {
                plc = sig.getPlcId();
                cmd = sig.getCmd();
                filterSignal.add(sig);
            }
        }
        return filterSignal;
    }

    /**
     * Get signal elements from calculations with some following conditions:
     * + Signal which owns calculations must be active -> exist in list of all signals active in system.
     * + Each calculation must have signal one is not null.
     * + Each calculation must have both signal two and method two are not null.
     * The data return of this method is the dto contains signal element name and its calculation.
     */
    @Cacheable(value = CALCULATED_SIGNALS)
    public List<SignalCalculatedDTO> getAllSignalFromCalculations() {
        logger.debug("============================== Get signals from calculations ==============================");
        List<Calculations> calculationsList = calculationsRepository.findAll();
        List<String> listAllSignals = getAllSignalActive()
            .parallelStream()
            .map(Signal::getName)
            .collect(Collectors.toList());
        List<SignalCalculatedDTO> dataReturn = new ArrayList<>();
        calculationsList.forEach(item -> {
            if (listAllSignals.contains(item.getNewSignalName())) {
                if (item.getSignalOne() != null) {
                    dataReturn.add(new SignalCalculatedDTO(item.getSignalOne().getName(), item));
                }
                if (item.getSignalTwo() != null && item.getMethodTwo() != null) {
                    dataReturn.add(new SignalCalculatedDTO(item.getSignalTwo().getName(), item));
                }
            }
        });
        return dataReturn;
    }

    // Delete all caches
    @Caching(evict = {
        @CacheEvict(value = INTENSIVE_LIST, allEntries = true),
        @CacheEvict(value = ALL_SIGNAL_ACTIVE, allEntries = true),
        @CacheEvict(value = CALCULATED_SIGNALS, allEntries = true)
    })
    public void refreshAllCaches() {
        logger.info("Request to refresh all caches for signal service");
        getAllSignalActive();
    }

    public Signal findSignalByName(String name) {
        return signalRepository.findByNameAndIsDeletedFalse(name);
    }

    @Async
    public void sendDataToSocket(List<SignalData> listData, List<SignalError> listError, List<Signal> allSignals) {
        listData.sort(Comparator.comparing(SignalData::getTimestamp));
        listError.sort(Comparator.comparing(SignalError::getTimestamp));
        listData.forEach(signalData -> {
            Optional<Signal> optionalSignal = allSignals
                .parallelStream()
                .filter(item -> item.getName().equalsIgnoreCase(signalData.getName()))
                .findFirst();
            if (optionalSignal.isPresent()) {
                Signal signal = optionalSignal.get();
                HashMap<String, Object> aSignalDataCus = new HashMap<>();
                //if danger <= 0 means can not find datatype so can not compare with threshold OR INTENSIVE
                //danger=3: normal, danger= 2 || 4: warning, danger = 1 || 5: error
                aSignalDataCus.put("id", signalData.getId());
                aSignalDataCus.put("name", signalData.getName());
                if (signal.getDataType().getName().equalsIgnoreCase(SignalConstants.DATA_TYPE_BOOL)) {
                    String displayValue;
                    if ((double) signalData.getValues() == 1) {
                        displayValue = signal.getDisplayTrue();
                    } else {
                        displayValue = signal.getDisplayFalse();
                    }
                    aSignalDataCus.put("values", displayValue);
                } else {
                    aSignalDataCus.put("values", signalData.getValues());
                }
                aSignalDataCus.put("timestamp", signalData.getTimestamp());
                aSignalDataCus.put("danger", commonUtil.checkStatus(signal, signalData));
                simpMessagingTemplate.convertAndSend("/topic/signalData", aSignalDataCus);
            }
        });
        listError.forEach(signalError -> {
            Optional<Signal> optionalSignal = allSignals
                .parallelStream()
                .filter(item -> item.getName().equalsIgnoreCase(signalError.getName()))
                .findFirst();
            if (optionalSignal.isPresent()) {
                Signal signal = optionalSignal.get();
                ErrorDataDTO errorDataDTO = commonUtil.errorDataDTOMapping(signalError, signal.getDigit() != null ?
                    Integer.parseInt(signal.getDigit()) : 0, signal.getDataType().getName());
                simpMessagingTemplate.convertAndSend("/topic/errorData", errorDataDTO);
            }
        });
    }
}
