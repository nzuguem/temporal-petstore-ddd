package me.nzuguem.petstore.shared.api.order.models;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record MarkOrderCompleteRequest(
        String customerEmail,
        ZonedDateTime orderDate,
        UUID transactionId,
        String orderNumber,
        List<Product> products,
        double orderTotal
) {
}
