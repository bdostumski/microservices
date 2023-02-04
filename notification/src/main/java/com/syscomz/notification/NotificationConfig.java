package com.syscomz.notification;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    // The value comes from application.yml file more specific rabbitmq.exchanges.internal which is equal to internal.exchanges
    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    // Same example as above
    @Value("${rabbitmq.queue.notification}")
    private String notificationQueue;

    // Same example as above
    @Value("${rabbitmq.routing-keys.internal-notification}")
    private String internalNotificationRoutingKey;

    // Define the Exchange name
    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    // Define the Queue name
    @Bean
    public Queue notificationQueue() {
        return new Queue(this.notificationQueue);
    }

    // Bind Exchange and Queue together
    @Bean
    public Binding internalToNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(internalTopicExchange())
                .with(this.getInternalNotificationRoutingKey());
    }

    public String getInternalExchange() {
        return internalExchange;
    }

    public String getNotificationQueue() {
        return notificationQueue;
    }

    public String getInternalNotificationRoutingKey() {
        return internalNotificationRoutingKey;
    }
}
