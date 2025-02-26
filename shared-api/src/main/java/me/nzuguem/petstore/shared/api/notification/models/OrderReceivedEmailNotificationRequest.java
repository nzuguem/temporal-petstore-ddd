package me.nzuguem.petstore.shared.api.notification.models;

import lombok.Builder;
import me.nzuguem.petstore.shared.api.order.models.Product;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderReceivedEmailNotificationRequest(
        String type,
        UUID transactionNumber,
        String customerEmail,
        ZonedDateTime orderDate,
        List<Product> products
) {

    public static class OrderReceivedEmailNotificationRequestBuilder {
        private String type = "normal";
    }
}
