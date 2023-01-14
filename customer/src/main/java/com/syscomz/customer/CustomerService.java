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
                "http://localhost:8081/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );

        if(fraudCheckResponse.isFraudster())
            throw new IllegalStateException("fraudster");

        // TODO: 1/14/23 send notification
    }
}
