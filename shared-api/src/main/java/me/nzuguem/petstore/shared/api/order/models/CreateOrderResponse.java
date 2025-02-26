package me.nzuguem.petstore.shared.api.order.models;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record CreateOrderResponse(
        String customerEmail,
        ZonedDateTime orderDate,
        UUID transactionId,
        String orderNumber,
        OrderStatus status,
        double orderTotal
) {
}
