package com.syscomz.notification.rabbitmq;

import com.syscomz.clients.notification.NotificationRequest;
import com.syscomz.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    // Get data from RabbitMQ -> Queues -> Payload and save it to the database
    // Listens for the messages in Queue
    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void consumer(NotificationRequest notificationRequest) {
        log.info("Consumer {} from queue", notificationService);
        notificationService.send(notificationRequest);
    }

}
