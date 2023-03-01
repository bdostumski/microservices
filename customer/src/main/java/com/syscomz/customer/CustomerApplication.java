package com.syscomz.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(
        // this is done to unlock the ability to inject RabbitMQMessagesProducer
        scanBasePackages = {
                "com.syscomz.customer",
                "com.syscomz.amqp",
        }
)
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.syscomz.clients" // this is needed because client lives in a different package
)
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties") // this is clients-default.properties, clients-docker.properties, and clients-kube.properties. where the spring.profiles.active give the specific name for the environment
})
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
