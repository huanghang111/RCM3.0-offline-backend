package com.bosch.rcm.service.consumer;

import com.bosch.rcm.domain.*;
import com.bosch.rcm.domain.constant.SignalConstants;
import com.bosch.rcm.repository.SignalDataRepository;
import com.bosch.rcm.repository.SignalErrorRepository;
import com.bosch.rcm.service.SignalDataService;
import com.bosch.rcm.service.SignalService;
import com.bosch.rcm.service.dto.SignalCalculatedDTO;
import com.bosch.rcm.service.util.CommonUtil;
import com.bosch.rcm.service.util.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Async
@Component
public class SignalDataConsumer {
    private final Logger logger = LoggerFactory.getLogger(SignalDataConsumer.class);
    private final SignalDataRepository signalDataRepository;
    private final SignalErrorRepository signalErrorRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SignalService signalService;
    private final SignalDataService signalDataService;
    private final CommonUtil commonUtil;
    @Resource(name = "latestErrorData")
    RcmLatestErrorData latestErrorData;
    private final DateUtil dateUtil;

    public SignalDataConsumer(SignalDataRepository signalDataRepository, SignalErrorRepository signalErrorRepository, SignalService signalService,
                              SignalDataService signalDataService, CommonUtil commonUtil, DateUtil dateUtil) {
        this.signalDataRepository = signalDataRepository;
        this.signalErrorRepository = signalErrorRepository;
        this.signalService = signalService;
        this.signalDataService = signalDataService;
        this.commonUtil = commonUtil;
        this.dateUtil = dateUtil;
    }

    @RabbitListener(queues = "${queue.mqtt-data.name}")
    public void receive(Message signalData) throws IOException {
        //org.springframework.amqp.core.Message
        String data = new String(signalData.getBody(), StandardCharsets.UTF_8);
        List<Map<String, Object>> myObjects = objectMapper.readValue(data, new TypeReference<List<Map<String, Object>>>() {
        });
        saveData(myObjects);
    }

    @RabbitListener(queues = "${queue.tcp-data.name}")
    public void receiveFromTcpIp(Message signalData) throws IOException {
        String data = new String(signalData.getBody(), StandardCharsets.UTF_8);
        List<Map<String, Object>> myObjects = objectMapper.readValue(data, new TypeReference<List<Map<String, Object>>>() {
        });
        saveData(myObjects);
    }

    void saveData(List<Map<String, Object>> data) {
        List<SignalData> list = new ArrayList<>();
        List<SignalError> errorList = new ArrayList<>();
        List<Signal> listAllSignals = signalService.getAllSignalActive();
        List<SignalCalculatedDTO> signalDataInCache = signalDataService.getAllLatestData();
        // Get one row values receive from Rabbit MQ
        for (Map<String, Object> row : data) {
            // At each row, get array of values contain {values and timestamps}
            List<Map<String, Object>> arrayValues = (List<Map<String, Object>>) row.get("values");
            // Check if signal name received is exist in database
            String name = (String) row.get("name");
            Optional<Signal> checkSignal = listAllSignals
                .parallelStream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst();
            if (checkSignal.isPresent()) {
                Signal signal = checkSignal.get();
                // Get each value of array of values contain {values and timestamps} above
                for (Map<String, Object> eachValue : arrayValues) {
                    SignalData signalDataSave = new SignalData();
                    signalDataSave.setName(signal.getName());
                    filterSignalError(errorList, eachValue, signal);
                    createSignalData(signalDataSave, eachValue);
                    list.add(signalDataSave);
                }
                List<SignalCalculatedDTO> calculatedSigList = signalDataInCache
                    .parallelStream()
                    .filter(item -> item.getSigElementName().equalsIgnoreCase(signal.getName()))
                    .collect(Collectors.toList());
                for (SignalCalculatedDTO oneCalculatedSig : calculatedSigList) {
                    Calculations calculations = oneCalculatedSig.getCalculations();
                    List<SignalData> oneCalculatedData = list
                        .parallelStream()
                        .filter(it -> it.getName().equalsIgnoreCase(oneCalculatedSig.getSigElementName()))
                        .collect(Collectors.toList());
                    if (oneCalculatedData.isEmpty()) {
                        continue;
                    }
                    if (calculations.getSignalTwo() != null) {
                        List<SignalData> anotherElementData = new ArrayList<>();
                        if (calculations.getSignalTwo().getName().equalsIgnoreCase(calculations.getSignalOne().getName())) {
                            signalDataService.calculatedSignalData(calculations, oneCalculatedData, oneCalculatedData, listAllSignals);
                        } else {
                            if (calculations.getSignalTwo().getName().equalsIgnoreCase(oneCalculatedSig.getSigElementName())) {
                                Optional<SignalCalculatedDTO> elementTwo = signalDataInCache
                                    .parallelStream()
                                    .filter(o -> o.getCalculations().getNewSignalName().equalsIgnoreCase(calculations.getNewSignalName())
                                        && o.getSigElementName().equalsIgnoreCase(calculations.getSignalOne().getName()))
                                    .findFirst();
                                if (elementTwo.isPresent()) {
                                    anotherElementData.add(elementTwo.get().getSigElementData());
                                    signalDataService.calculatedSignalData(calculations, anotherElementData, oneCalculatedData, listAllSignals);
                                }
                            } else {
                                Optional<SignalCalculatedDTO> elementTwo = signalDataInCache
                                    .parallelStream()
                                    .filter(o -> o.getCalculations().getNewSignalName().equalsIgnoreCase(calculations.getNewSignalName())
                                        && o.getSigElementName().equalsIgnoreCase(calculations.getSignalTwo().getName()))
                                    .findFirst();
                                if (elementTwo.isPresent()) {
                                    anotherElementData.add(elementTwo.get().getSigElementData());
                                    signalDataService.calculatedSignalData(
                                        calculations, oneCalculatedData, anotherElementData, listAllSignals);
                                }
                            }
                        }
                    } else {
                        signalDataService.calculatedSignalData(calculations, oneCalculatedData, new ArrayList<>(), listAllSignals);
                    }
                    oneCalculatedData.sort(Comparator.comparing(SignalData::getTimestamp).reversed());
                    signalDataInCache = signalDataService.updateLatestData(list.get(0));
                }
            }
        }
        list = signalDataRepository.saveAll(list);
        errorList = signalErrorRepository.saveAll(errorList);
        updateApplicationData(errorList);
        signalService.sendDataToSocket(list, errorList, listAllSignals);
    }

    private void updateApplicationData(List<SignalError> signalList) {
        signalList.forEach(signalError -> {
            String threshId = signalError.getThreshold().getId();
            Integer errCount = latestErrorData.getCountErrors().get(threshId);
            Integer pieCount = latestErrorData.getCountDataForPieChart().get(threshId);
            if (errCount != null && errCount > 0) {
                latestErrorData.getCountErrors().replace(threshId, (errCount + 1));
                latestErrorData.getCountDataForPieChart().replace(threshId, pieCount != null ? (pieCount + 1) : 1);
            } else {
                latestErrorData.getFirstErrors().put(threshId, signalError);
                latestErrorData.getCountErrors().put(threshId, 1);
                latestErrorData.getCountDataForPieChart().put(threshId, pieCount != null ? (pieCount + 1) : 1);
            }
        });
    }

    private void filterSignalError(List<SignalError> errorList, Map<String, Object> eachValue, Signal signal) {
        Object valueObject = eachValue.get("value");
        double value;
        if (valueObject instanceof Double) {
            value = (Double) valueObject;
        } else {
            // Because of customers want values return MQTT method may have "True" or "False"
            String stringValue = valueObject instanceof String ? (String) valueObject : null;
            if (stringValue == null) {
                return;
            }
            if (stringValue.toUpperCase().equalsIgnoreCase("TRUE")) {
                value = 1d;
            } else if (stringValue.toUpperCase().equalsIgnoreCase("FALSE")) {
                value = 0d;
            } else {
                return;
            }
        }
        if (signal != null) {
            if (signal.getDataType().getName().equals(SignalConstants.DATA_TYPE_BOOL)) {
                signalBoolError(errorList, value, signal, eachValue);
            } else if (!signal.getDataType().getName().equals(SignalConstants.DATA_TYPE_INTENSIVE)) {
                signalNumericError(errorList, value, signal, eachValue);
            }
        }
    }

    private void signalNumericError(List<SignalError> errorList, double value, Signal signal, Map<String, Object> eachValue) {
        Threshold thresholdTooLow = commonUtil.extractThresholdByLevel(signal.getThresholds(), SignalConstants.THRESHOLD_MIN_MIN);
        Threshold thresholdLow = commonUtil.extractThresholdByLevel(signal.getThresholds(), SignalConstants.THRESHOLD_MIN);
        Threshold thresholdHigh = commonUtil.extractThresholdByLevel(signal.getThresholds(), SignalConstants.THRESHOLD_MAX);
        Threshold thresholdTooHigh = commonUtil.extractThresholdByLevel(signal.getThresholds(), SignalConstants.THRESHOLD_MAX_MAX);
        if (thresholdTooLow == null || thresholdLow == null || thresholdHigh == null || thresholdTooHigh == null) {
            return;
        }
        // Too avoid mistake in threshold values
        double tooLow;
        double low;
        double high;
        double tooHigh;
        try {
            tooLow = Double.parseDouble(thresholdTooLow.getValues());
            low = Double.parseDouble(thresholdLow.getValues());
            high = Double.parseDouble(thresholdHigh.getValues());
            tooHigh = Double.parseDouble(thresholdTooHigh.getValues());
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return;
        }
        // Find out error
        if (value > high || value < low) {
            SignalError signalError = new SignalError();
            signalError.setName(signal.getName());
            // Error at threshold too low
            if (value < Double.parseDouble(thresholdTooLow.getValues())) {
                createSignalError(value, thresholdTooLow, signalError, eachValue);
            }
            // Error at threshold low
            else if (value >= tooLow && value < low) {
                createSignalError(value, thresholdLow, signalError, eachValue);
            }
            // Error at threshold high
            else if (value > high && value <= tooHigh) {
                createSignalError(value, thresholdHigh, signalError, eachValue);
            }
            // Error at threshold too high
            else {
                createSignalError(value, thresholdTooHigh, signalError, eachValue);
            }
            errorList.add(signalError);
        }
    }

    private void signalBoolError(List<SignalError> errorList, double value, Signal signal, Map<String, Object> eachValue) {
        Threshold thresholdBool = commonUtil.extractThresholdByLevel(signal.getThresholds(), "");
        // Case of value receive is 0 or 1
        // Error at threshold true || Error at threshold false
        if (thresholdBool != null) {
            if ((value == 0d && thresholdBool.getValues().equalsIgnoreCase(SignalConstants.THRESHOLD_VALUE_FALSE))
                || (value == 1 && thresholdBool.getValues().equalsIgnoreCase(SignalConstants.THRESHOLD_VALUE_TRUE))) {
                SignalError signalError = new SignalError();
                signalError.setName(signal.getName());
                createSignalError(value, thresholdBool, signalError, eachValue);
                errorList.add(signalError);
            }
        }
    }

    private void createSignalError(double value, Threshold threshold, SignalError signalError, Map<String, Object> eachValue) {
        Instant instant = Instant.parse((String) eachValue.get("timestamp"));
        signalError.setOrder(dateUtil.getYearFromInstant(instant));
        signalError.setTimestamp(instant);
        signalError.setValues(value);
        signalError.setThreshold(threshold);
    }

    private void createSignalData(SignalData signalDataSave, Map<String, Object> eachValue) {
        Instant instant = Instant.parse((String) eachValue.get("timestamp"));
        signalDataSave.setTimestamp(instant);
        signalDataSave.setOrder(dateUtil.getYearFromInstant(instant));
        Object valueObject = eachValue.get("value");
        if (!(valueObject instanceof Double)
            && !(valueObject instanceof Integer)
            && !(valueObject instanceof ArrayList)) {
            String valueAsString = eachValue.get("value").toString();
            if (valueAsString.toUpperCase().equalsIgnoreCase("TRUE")) {
                signalDataSave.setValues(1.0);
                return;
            }
            if (valueAsString.toUpperCase().equalsIgnoreCase("FALSE")) {
                signalDataSave.setValues(0d);
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            List<Object> listIntensiveValues = new ArrayList<>();
            try {
                listIntensiveValues.add(mapper.readValue(valueAsString, List.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
            signalDataSave.setValues(listIntensiveValues);
        } else {
            signalDataSave.setValues(valueObject);
        }
    }
}
