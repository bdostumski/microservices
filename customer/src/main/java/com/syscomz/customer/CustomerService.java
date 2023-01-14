package com.syscomz.customer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository, RestTemplate restTemplate) {
    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();

        // TODO: 1/14/23 check if email valid 
        // TODO: 1/14/23 check if email not taken
        customerRepository.saveAndFlush(customer); // flushes the data immediately during the execution

        // TODO: 1/14/23 check if fraudster
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                // "http://localhost:8081/api/v1/fraud-check/{customerId}", // this is default configuration without Eureka Server
                // With Eureka Server we can remove localhost and the port and replace it wit the name of the microservice
                // it can be seen into http://localhost:8761/ port is given by me into application.yml file
                "http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );

        if(fraudCheckResponse.isFraudster())
            throw new IllegalStateException("fraudster");

        // TODO: 1/14/23 send notification
    }
}
