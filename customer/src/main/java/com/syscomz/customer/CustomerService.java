package com.syscomz.customer;

import com.syscomz.clients.fraud.FraudCheckResponse;
import com.syscomz.clients.fraud.FraudClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(
        CustomerRepository customerRepository,
        RestTemplate restTemplate,
        FraudClient fraudClient
) {

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
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster())
            throw new IllegalStateException("fraudster");

        // TODO: 1/14/23 send notification
    }

}
