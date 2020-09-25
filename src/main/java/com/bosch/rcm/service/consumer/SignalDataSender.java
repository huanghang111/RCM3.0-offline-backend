package com.bosch.rcm.service.consumer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SignalDataSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.intensive-queue.name}")
    private String QUEUE_INTENSIVE;

    @Autowired
    public SignalDataSender(RabbitTemplate rabbitTemplate) {this.rabbitTemplate = rabbitTemplate;}

    public void sendMessage(String message) {
        this.rabbitTemplate.convertAndSend(QUEUE_INTENSIVE, message);
    }
}
