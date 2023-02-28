package com.syscomz.clients.fraud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Configuration for Eureka Server
@FeignClient(
        name = "fraud",
        url = "${clients.fraud.url}"
        // path = "api/v1/fraud-check" // path can be added here also into the method declaration
)
// This interface will target FraudController into Fraud microservice
public interface FraudClient {

    // @GetMapping(path = "{customerId}") // if the path was specified above
    @GetMapping(path = "api/v1/fraud-check/{customerId}")
    FraudCheckResponse isFraudster(@PathVariable("customerId") Integer customerId);

}