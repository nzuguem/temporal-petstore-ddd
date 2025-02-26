package me.nzuguem.petstore.shared.api.payment.models;

import lombok.Builder;
import me.nzuguem.petstore.shared.api.order.models.CreditCardInfo;

import java.util.UUID;

@Builder
public record DebitCreditCardRequest(
        String requestedByUser,
        String requestedByHost,
        UUID transactionId,
        CreditCardInfo creditCard,
        double amount,
        String customerEmail
) {
}
