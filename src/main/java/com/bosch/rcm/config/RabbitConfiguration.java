package com.bosch.rcm.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration implements RabbitListenerConfigurer {

    @Value("${queue.intensive-queue.name}")
    private String QUEUE_INTENSIVE;

    @Value("${queue.dead-intensive-queue.name}")
    private String QUEUE_DEAD_INTENSIVE;

    @Value("${exchange.intensive-exchange.name}")
    private String EXCHANGE_INTENSIVE;

    @Value("${queue.signal-setting-queue.name}")
    private String SIGNAL_SETTING_QUEUE;

    @Value("${exchange.signal-setting-exchange.name}")
    private String SIGNAL_SETTING_EXCHANGE;

    @Bean
    Queue ordersQueue() {

        return QueueBuilder.durable(QUEUE_INTENSIVE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", QUEUE_DEAD_INTENSIVE)
                .withArgument("x-message-ttl", 5000)
                .build();
    }

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(QUEUE_DEAD_INTENSIVE).build();
    }

    @Bean
    Exchange ordersExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_INTENSIVE).build();
    }

    @Bean
    Binding binding(Queue ordersQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(ordersQueue).to(ordersExchange).with(QUEUE_INTENSIVE);
    }

    @Bean
    public Queue signalSettingQueue() {
        return QueueBuilder.durable(SIGNAL_SETTING_QUEUE).build();
    }

    @Bean
    public TopicExchange signalSettingExchange() {
        return (TopicExchange) ExchangeBuilder.topicExchange(SIGNAL_SETTING_EXCHANGE).build();
    }

    @Bean
    public Binding bindingSignalSettingQueue(Queue signalSettingQueue, TopicExchange signalSettingExchange) {
        return BindingBuilder.bind(signalSettingQueue).to(signalSettingExchange).with(SIGNAL_SETTING_QUEUE);
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }
}
