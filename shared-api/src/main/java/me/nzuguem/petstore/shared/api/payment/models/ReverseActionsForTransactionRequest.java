package me.nzuguem.petstore.shared.api.payment.models;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ReverseActionsForTransactionRequest(
        String requestedByUser,
        String requestedByHost,
        UUID transactionId
) {
}
