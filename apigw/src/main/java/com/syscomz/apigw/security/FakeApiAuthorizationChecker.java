package com.syscomz.apigw.security;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("fake")
public class FakeApiAuthorizationChecker implements ApiKeyAuthorizationChecker {

    private final static Map<String, List<String>> keys = Map.of(
            "supersecure", List.of("customer"), // the supersecure key can access only customer application
            "supersecure123", List.of("customer", "fraud") // the supersecure123 key can access only customer, and fraud application
    );

    @Override
    public boolean isAuthorized(String key, String application) {
        return keys.getOrDefault(key, List.of()).stream()
                .anyMatch(k -> k.contains(application));
    }

}
