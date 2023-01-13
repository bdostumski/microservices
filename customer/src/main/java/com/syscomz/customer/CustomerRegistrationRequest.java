package com.syscomz.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {}
