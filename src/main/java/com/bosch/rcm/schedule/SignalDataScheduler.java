//package com.bosch.rcm.schedule;
//
//import com.bosch.rcm.domain.*;
//import com.bosch.rcm.domain.constant.SignalConstants;
//import com.bosch.rcm.repository.SignalDataRepository;
//import com.bosch.rcm.repository.SignalErrorRepository;
//import com.bosch.rcm.service.DiagnoseService;
//import com.bosch.rcm.service.SignalService;
//import com.bosch.rcm.service.util.DateUtil;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@EnableAsync
//@Component
//public class SignalDataScheduler {
//    private final SignalDataRepository signalDataRepository;
//    private final SignalErrorRepository signalErrorRepository;
//    private final DiagnoseService diagnoseService;
//    private final SignalService signalService;
//    private final DateUtil dateUtil;
//
//    public SignalDataScheduler(SignalDataRepository signalDataRepository, SignalErrorRepository signalErrorRepository,
//                               DiagnoseService diagnoseService, SignalService signalService, DateUtil dateUtil) {
//        this.signalDataRepository = signalDataRepository;
//        this.signalErrorRepository = signalErrorRepository;
//        this.diagnoseService = diagnoseService;
//        this.signalService = signalService;
//        this.dateUtil = dateUtil;
//    }
//
//    @Async
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void getDataForFaultSignal() {
//        List<Signal> faultList = signalService.getListFaultSignals();
//        for (Signal fault : faultList) {
//            Calculations faultCalculation = fault.getCalculations();
//            if (faultCalculation == null) {
//                // Case signal fault create by import file config
//                continue;
//            }
//            Signal sigOne = faultCalculation.getSignalOne();
//            Signal sigTwo = faultCalculation.getSignalTwo();
//            if (checkSignalsDeleted(sigOne, sigTwo)) { continue; }
//            Method methodOne = faultCalculation.getMethodOne();
//            Method methodTwo = faultCalculation.getMethodTwo();
//            List<SignalData> listFaultData = new ArrayList<>();
//            List<SignalError> listFaultError = new ArrayList<>();
//            Instant timeTo = Instant.now();
//            Instant timeFrom = timeTo.minusSeconds(60);
//            List<SignalData> sigOneData = signalDataRepository.findAllByNameAndTimestampBetweenOrderByTimestampDesc(
//                sigOne.getName(), timeFrom, timeTo);
//            List<SignalData> sigTwoData = new ArrayList<>();
//            if (sigTwo != null) {
//                sigTwoData = signalDataRepository.findAllByNameAndTimestampBetweenOrderByTimestampDesc(
//                    sigTwo.getName(), timeFrom, timeTo);
//            }
//            if (sigOneData.isEmpty()) {
//                continue;
//            }
//            for (SignalData data1 : sigOneData) {
//                Instant dataTime1 = data1.getTimestamp();
//                SignalData data2 = null;
//                if (sigTwo != null) {
//                    data2 = compareSignalData(data1, sigTwoData);
//                    if (data2 == null) {
//                        // Case signalTwo exists but it doesnt have data
//                        continue;
//                    }
//                }
//                Double faultValue = null;
//                faultValue = diagnoseService.methodResolve(faultValue, sigOne, methodOne, methodTwo, data1, data2);
//                if (faultValue != null) {
//                    SignalData faultData = new SignalData();
//                    faultData.setName(fault.getName());
//                    faultData.setValues(faultValue.intValue());
//                    faultData.setTimestamp(dataTime1);
//                    listFaultData.add(faultData);
//                    Threshold threshold = signalService.extractThresholdByLevel(fault.getThresholds(), "");
//                    if (threshold != null) {
//                        if ((faultValue == 1 && threshold.getValues().equalsIgnoreCase(SignalConstants.THRESHOLD_VALUE_TRUE))
//                            || (faultValue == 0 && threshold.getValues().equalsIgnoreCase(SignalConstants.THRESHOLD_VALUE_FALSE))) {
//                            SignalError faultError = new SignalError();
//                            faultError.setName(fault.getName());
//                            faultError.setValues(faultValue.intValue());
//                            faultError.setThreshold(threshold);
//                            faultError.setTimestamp(dataTime1);
//                            listFaultError.add(faultError);
//                        }
//                    }
//                }
//            }
//            listFaultData = signalDataRepository.saveAll(listFaultData);
//            listFaultError = signalErrorRepository.saveAll(listFaultError);
//            signalService.sendDataToSocket(listFaultData, listFaultError);
//        }
//    }
//
//    @Async
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void getDataForNormalSignal() {
//        List<Signal> normalList = signalService.getListNormalSignals();
//        for (Signal oneNormal : normalList) {
//            Calculations normalCal = oneNormal.getCalculations();
//            Signal sigOne = normalCal.getSignalOne();
//            Signal sigTwo = normalCal.getSignalTwo();
//            if (checkSignalsDeleted(sigOne, sigTwo)) { continue; }
//            Method methodOne = normalCal.getMethodOne();
//            Method methodTwo = normalCal.getMethodTwo();
//            List<SignalData> listNormalData = new ArrayList<>();
//            List<SignalError> listNormalError = new ArrayList<>();
//            Instant timeTo = Instant.now();
//            Instant timeFrom = timeTo.minusSeconds(60);
//            List<SignalData> sigOneData = signalDataRepository.findAllByNameAndTimestampBetweenOrderByTimestampDesc(
//                sigOne.getName(), timeFrom, timeTo);
//            List<SignalData> sigTwoData = new ArrayList<>();
//            if (sigTwo != null) {
//                sigTwoData = signalDataRepository.findAllByNameAndTimestampBetweenOrderByTimestampDesc(
//                    sigTwo.getName(), timeFrom, timeTo);
//            }
//            if (sigOneData.isEmpty()) {
//                continue;
//            }
//            for (SignalData data1 : sigOneData) {
//                Instant dataTime1 = data1.getTimestamp();
//                SignalData data2 = null;
//                if (sigTwo != null) {
//                    data2 = compareSignalData(data1, sigTwoData);
//                    if (data2 == null) {
//                        // Case signalTwo exists but it doesnt have data
//                        continue;
//                    }
//                }
//                Double normalValue = null;
//                normalValue = diagnoseService.methodResolve(normalValue, sigOne, methodOne, methodTwo, data1, data2);
//                if (normalValue != null) {
//                    SignalData normalData = new SignalData();
//                    normalData.setName(oneNormal.getName());
//                    if (oneNormal.getDataType().getName().equalsIgnoreCase(SignalConstants.DATA_TYPE_BOOL)) {
//                        normalData.setValues(normalValue.intValue());
//                    } else {
//                        normalData.setValues(normalValue);
//                    }
//                    normalData.setTimestamp(dataTime1);
//                    listNormalData.add(normalData);
//                    Set<Threshold> normalThreshSet = oneNormal.getThresholds();
//                    if (normalThreshSet != null && normalThreshSet.size() > 0) {
//                        if (oneNormal.getDataType().getName().equalsIgnoreCase(SignalConstants.DATA_TYPE_BOOL)) {
//                            // Boolean signal
//                            Threshold threshold = signalService.extractThresholdByLevel(normalThreshSet, "");
//                            if (threshold != null) {
//                                if ((normalValue == 1 && threshold.getValues().equals(SignalConstants.THRESHOLD_VALUE_TRUE))
//                                    || (normalValue == 0 && threshold.getValues().equals(SignalConstants.THRESHOLD_VALUE_FALSE))) {
//                                    SignalError faultError = new SignalError();
//                                    faultError.setName(oneNormal.getName());
//                                    faultError.setValues(normalValue.intValue());
//                                    faultError.setTimestamp(dataTime1);
//                                    faultError.setThreshold(threshold);
//                                    listNormalError.add(faultError);
//                                }
//                            }
//                        } else {
//                            // Numeric signal
//                            Threshold thresholdMinMin = signalService.extractThresholdByLevel(normalThreshSet, SignalConstants.THRESHOLD_MIN_MIN);
//                            Threshold thresholdMin = signalService.extractThresholdByLevel(normalThreshSet, SignalConstants.THRESHOLD_MIN);
//                            Threshold thresholdMax = signalService.extractThresholdByLevel(normalThreshSet, SignalConstants.THRESHOLD_MAX);
//                            Threshold thresholdMaxMax = signalService.extractThresholdByLevel(normalThreshSet, SignalConstants.THRESHOLD_MAX_MAX);
//                            // Find out error
//                            if (thresholdMinMin != null && thresholdMin != null && thresholdMax != null && thresholdMaxMax != null) {
//                                if (normalValue > Double.parseDouble(thresholdMax.getValues()) || normalValue < Double.parseDouble(thresholdMin.getValues())) {
//                                    SignalError faultError = new SignalError();
//                                    // Error at threshold too low
//                                    if (normalValue < Double.parseDouble(thresholdMinMin.getValues())) {
//                                        faultError.setThreshold(thresholdMinMin);
//                                    }
//                                    // Error at threshold low
//                                    else if (normalValue >= Double.parseDouble(thresholdMinMin.getValues()) && normalValue < Double.parseDouble(thresholdMin.getValues())) {
//                                        faultError.setThreshold(thresholdMin);
//                                    }
//                                    // Error at threshold high
//                                    else if (normalValue > Double.parseDouble(thresholdMax.getValues()) && normalValue <= Double.parseDouble(thresholdMaxMax.getValues())) {
//                                        faultError.setThreshold(thresholdMax);
//                                    }
//                                    // Error at threshold too high
//                                    else {
//                                        faultError.setThreshold(thresholdMaxMax);
//                                    }
//                                    faultError.setName(oneNormal.getName());
//                                    faultError.setValues(normalValue);
//                                    faultError.setTimestamp(dataTime1);
//                                    listNormalError.add(faultError);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            listNormalData = signalDataRepository.saveAll(listNormalData);
//            listNormalError = signalErrorRepository.saveAll(listNormalError);
//            signalService.sendDataToSocket(listNormalData, listNormalError);
//        }
//    }
//
//    private boolean checkSignalsDeleted(Signal sigOne, Signal sigTwo) {
//        boolean sigOneDeleted = sigOne.getDeleted();
//        boolean sigTwoDeleted = false;
//        if (sigTwo != null) {
//            sigTwoDeleted = sigTwo.getDeleted();
//        }
//        // Case signal one or two have been deleted
//        return sigOneDeleted || sigTwoDeleted;
//    }
//
//    private SignalData compareSignalData(SignalData sigData1, List<SignalData> signalDataList2) {
//        if (signalDataList2 == null || signalDataList2.size() == 0) {
//            return null;
//        }
//        Optional<SignalData> result = signalDataList2.
//            parallelStream().
//            filter(item -> dateUtil.getMilSecFromTime1AndTime2(sigData1.getTimestamp(), item.getTimestamp()) <= 500).
//            findFirst();
//        return result.orElse(null);
//    }
//}
