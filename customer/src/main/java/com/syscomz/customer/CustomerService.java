package com.syscomz.customer;

import com.syscomz.amqp.RabbitMQMessageProducer;
import com.syscomz.clients.fraud.FraudCheckResponse;
import com.syscomz.clients.fraud.FraudClient;
import com.syscomz.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    //        RestTemplate restTemplate,
//        NotificationClient notificationClient,


    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer); // flushes the data immediately during the execution

        // TODO: 1/14/23 check if fraudster
        // This was used  from RestTemplate and Eureka Server also, but now it will be replaced with configuration below
        // FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
        //         // "http://localhost:8081/api/v1/fraud-check/{customerId}", // this is default configuration without Eureka Server
        //         // With Eureka Server we can remove localhost and the port and replace it wit the name of the microservice
        //         // it can be seen into http://localhost:8761/ port is given by me into application.yml file
        //         "http://FRAUD/api/v1/fraud-check/{customerId}",
        //         FraudCheckResponse.class,
        //         customer.getId()
        // );

        // This is done by OpenFeign, and the module clients where is placed an interface with the path to the FraudClient microservice
        FraudCheckResponse fraudCheckResponse =
                fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to SYSCOMz...",
                        customer.getFirstName())
        );

        // send notification
        // after the RabbitMQ implementation we no longer need to send notifications directly to the Notification microservice
        //        notificationClient.sendNotification(notificationRequest);

        // Instead we send notifications directly to the Notification microservice, we will use RabbitMQ
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

    }

}
