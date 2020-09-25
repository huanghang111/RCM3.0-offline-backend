//package com.bosch.rcm.schedule;
//
//import com.bosch.rcm.domain.Signal;
//import com.bosch.rcm.domain.SignalData;
//import com.bosch.rcm.domain.SignalError;
//import com.bosch.rcm.domain.Threshold;
//import com.bosch.rcm.domain.constant.SignalConstants;
//import com.bosch.rcm.repository.SignalDataRepository;
//import com.bosch.rcm.repository.SignalErrorRepository;
//import com.bosch.rcm.service.SignalService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//@EnableAsync
//@Component
//public class GenerateData {
//    private final Logger logger = LoggerFactory.getLogger(GenerateData.class);
//    private final SignalService signalService;
//    private final SignalErrorRepository signalErrorRepository;
//    private final SignalDataRepository signalDataRepository;
//
//    public GenerateData(SignalService signalService, SignalErrorRepository signalErrorRepository, SignalDataRepository signalDataRepository) {
//        this.signalService = signalService;
//        this.signalErrorRepository = signalErrorRepository;
//        this.signalDataRepository = signalDataRepository;
//    }
//
//    @Async
//    @Scheduled(fixedRate = 1000)
//    public void generateForFault1() {
//        saveDataRaw("fault1223");
//        saveDataRaw("Temperature");
//    }
//
//    private void saveDataRaw(String name) {
//        List<SignalData> listData = new ArrayList<>();
//        List<SignalError> listError = new ArrayList<>();
//        Signal fault1 = signalService.findSignalByName(name);
//        Instant timeNow = Instant.now();
//        if (fault1 == null) {
//            return;
//        }
//        if (fault1.getDataType().getName().equalsIgnoreCase(SignalConstants.DATA_TYPE_BOOL)) {
//            Threshold threshold = fault1.getThresholds()
//                .stream()
//                .filter(item -> item.getLevel().equalsIgnoreCase(""))
//                .findAny()
//                .orElse(null);
//            if (threshold == null) {
//                return;
//            }
//            int rand = Calendar.getInstance().get(Calendar.SECOND);
//            int value = 0;
//            if (rand % 5 == 0) {
//                value = 1;
//            }
//            SignalData signalData = new SignalData();
//            signalData.setName(name);
//            signalData.setValues(value);
//            signalData.setTimestamp(timeNow);
//            listData.add(signalDataRepository.save(signalData));
//            if ((value == 0 && threshold.getValues().equalsIgnoreCase(SignalConstants.THRESHOLD_VALUE_FALSE)) ||
//                (value == 1 && threshold.getValues().equalsIgnoreCase(SignalConstants.THRESHOLD_VALUE_TRUE))) {
//                SignalError signalError = new SignalError();
//                signalError.setName(name);
//                signalError.setValues(value);
//                signalError.setTimestamp(timeNow);
//                signalError.setThreshold(threshold);
//                listError.add(signalErrorRepository.save(signalError));
//            }
//        } else {
//            double value = 1.5;
//            int rand = Calendar.getInstance().get(Calendar.SECOND);
//            value = rand * value * 10 / 3;
//            SignalData signalData = new SignalData();
//            signalData.setName(name);
//            signalData.setValues(value);
//            signalData.setTimestamp(timeNow);
//            listData.add(signalDataRepository.save(signalData));
//        }
//        signalService.sendDataToSocket(listData, listError, signalService.getAllSignalActive());
//    }
//}
