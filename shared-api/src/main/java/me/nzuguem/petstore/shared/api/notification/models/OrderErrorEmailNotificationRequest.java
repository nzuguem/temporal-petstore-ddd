package me.nzuguem.petstore.shared.api.notification.models;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record OrderErrorEmailNotificationRequest(
        String type,
        String customerEmail,
        String orderNumber,
        UUID transactionNumber,
        ZonedDateTime orderDate
) {

    public static class OrderErrorEmailNotificationRequestBuilder {
        private String type = "error";
    }
}
