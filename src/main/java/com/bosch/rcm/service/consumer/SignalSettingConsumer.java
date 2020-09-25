package com.bosch.rcm.service.consumer;

import com.bosch.rcm.domain.constant.SignalConstants;
import com.bosch.rcm.service.SignalDataService;
import com.bosch.rcm.service.SignalService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SignalSettingConsumer {

    private final SignalService signalService;
    private final SignalDataService signalDataService;

    public SignalSettingConsumer(SignalService signalService, SignalDataService signalDataService) {this.signalService = signalService;
        this.signalDataService = signalDataService;
    }

    @RabbitListener(queues = "${queue.signal-setting-queue.name}")
    public void signalSettingStatus(Message signalSetting) {
        String data = new String(signalSetting.getBody());
        if (data.equalsIgnoreCase(SignalConstants.SIGNAL_SETTING)) {
            signalService.refreshAllCaches();
            signalDataService.refreshDataCaches();
        }
    }
}
