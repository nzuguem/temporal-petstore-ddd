package me.nzuguem.petstore.shared.api.order.models;

import lombok.Builder;

import java.util.UUID;

@Builder
public record MarkOrderFailedRequest(
        UUID transactionId,
        String orderNumber,
        OrderFailureReason reason
) {
}
