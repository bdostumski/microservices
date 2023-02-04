package com.syscomz.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        // this is done to unlock the ability to inject RabbitMQMessagesProducer
        scanBasePackages = {
                "com.syscomz.customer",
                "com.syscomz.amqp"
        }
)
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.syscomz.clients" // this is needed because client lives in a different package
)
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
