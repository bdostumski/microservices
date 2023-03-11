package com.syscomz.apigw.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class ApiKeyAuthorizationFilter implements GlobalFilter, Ordered {

    private final FakeApiAuthorizationChecker fakeApiAuthorizationChecker;

    // ServerWebExchange -> Provides access to the HTTP request and response
    // GatewayFilterChain -> Contract to allow a WebFilter to delegate to the next in the chain. [chain two or more filters, fist pass one after that go to the second and so on...]
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("ApiKeyAuthorizationFilter... checking the key");

        Route attribute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String application = attribute.getId();

        List<String> apiKey = exchange.getRequest().getHeaders().get("ApiKey"); // get the key sent from postman in the header of the request

        if(application == null
                || (apiKey == null || apiKey.isEmpty())
                || !fakeApiAuthorizationChecker.isAuthorized(apiKey.get(0), application))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you are not authorized");

        return chain.filter(exchange); // send request to the next filter in the chain
    }

    // Give the order when to execute this filter. For example after or before other filters
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE; // it means that this filter will be the last
    }
}
