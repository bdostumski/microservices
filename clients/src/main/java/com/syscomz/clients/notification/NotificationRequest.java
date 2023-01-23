package com.syscomz.clients.notification;

public record NotificationRequest(
        Integer toCustomerid,
        String toCustomerEmail,
        String message
) {}
