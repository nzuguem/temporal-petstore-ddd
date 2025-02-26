package me.nzuguem.petstore.shared.api.order.models;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateOrderRequest(
        String requestedByUser,
        String requestedByHost,
        String customerEmail,
        ZonedDateTime orderDate,
        UUID transactionId,
        List<Product> products
) {
}
