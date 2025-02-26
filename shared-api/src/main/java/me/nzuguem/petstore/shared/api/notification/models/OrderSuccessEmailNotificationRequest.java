package me.nzuguem.petstore.shared.api.notification.models;

import lombok.Builder;
import me.nzuguem.petstore.shared.api.order.models.Product;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderSuccessEmailNotificationRequest(
        String type,
        UUID transactionNumber,
        String customerEmail,
        String orderNumber,
        ZonedDateTime orderDate,
        String trackingNumber,
        List<Product> products,
        double orderTotal
) {

    public static class OrderSuccessEmailNotificationRequestBuilder {
        private String type = "success";
    }
}
