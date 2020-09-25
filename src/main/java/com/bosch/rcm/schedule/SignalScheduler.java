package com.bosch.rcm.schedule;

import com.bosch.rcm.domain.Signal;
import com.bosch.rcm.domain.constant.SignalConstants;
import com.bosch.rcm.service.SignalErrorService;
import com.bosch.rcm.service.SignalService;
import com.bosch.rcm.service.consumer.SignalDataSender;
import com.bosch.rcm.service.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Async
@Component
public class SignalScheduler {
    private final Logger log = LoggerFactory.getLogger(SignalScheduler.class);
    private final SignalService signalService;
    private final SignalErrorService signalErrorService;
    private final DateUtil dateUtil;
    private final SignalDataSender signalDataSender;

    public SignalScheduler(SignalService signalService, SignalErrorService signalErrorService, DateUtil dateUtil, SignalDataSender signalDataSender) {
        this.signalService = signalService;
        this.signalErrorService = signalErrorService;
        this.dateUtil = dateUtil;
        this.signalDataSender = signalDataSender;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void checkSignalIntensive() {
        List<Signal> listIntensive = signalService.getListIntensiveSignals();
        if (listIntensive == null || listIntensive.size() == 0) {
            return;
        }
        for (Signal intensive : listIntensive) {
            if (intensive.getTriggerHour() == null || intensive.getTriggerMinute() == null) {
                continue;
            }
            int hourTrigger = intensive.getTriggerHour();
            int minTrigger = intensive.getTriggerMinute();
            Instant triggerTime = dateUtil.createInstantByParams(hourTrigger, minTrigger, 0);
            Long minutesBetween = dateUtil.getMinuteFromTimeToNow(triggerTime);
            String interval = intensive.getTriggerInterval();
            switch (interval) {
                case SignalConstants.TRIGGER_INTERVAL_10_MIN: {
                    if (minutesBetween % 10 == 0) {
                        log.info("Send message for set plc {} and cmd {} with interval {}", intensive.getPlcId(), intensive.getCmd(), interval);
                        signalDataSender.sendMessage(intensive.getName());
                    }
                    break;
                }
                case SignalConstants.TRIGGER_INTERVAL_1_HOUR: {
                    if (minutesBetween % 60 == 0) {
                        log.info("Send message for set plc {} and cmd {} with interval {}", intensive.getPlcId(), intensive.getCmd(), interval);
                        signalDataSender.sendMessage(intensive.getName());
                    }
                    break;
                }
                case SignalConstants.TRIGGER_INTERVAL_2_HOUR: {
                    if (minutesBetween % 120 == 0) {
                        log.info("Send message for set plc {} and cmd {} with interval {}", intensive.getPlcId(), intensive.getCmd(), interval);
                        signalDataSender.sendMessage(intensive.getName());
                    }
                    break;
                }
                case SignalConstants.TRIGGER_INTERVAL_8_HOUR: {
                    if (minutesBetween % 480 == 0) {
                        log.info("Send message for set plc {} and cmd {} with interval {}", intensive.getPlcId(), intensive.getCmd(), interval);
                        signalDataSender.sendMessage(intensive.getName());
                    }
                    break;
                }
                case SignalConstants.TRIGGER_INTERVAL_1_DAY: {
                    if (minutesBetween % 1440 == 0) {
                        log.info("Send message for set plc {} and cmd {} with interval {}", intensive.getPlcId(), intensive.getCmd(), interval);
                        signalDataSender.sendMessage(intensive.getName());
                    }
                    break;
                }
            }
        }
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void storeCacheToDatabase() {
        signalErrorService.storeData();
    }
}
