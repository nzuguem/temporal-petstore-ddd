package me.nzuguem.petstore.shared.api.payment.models;

import lombok.Builder;
import me.nzuguem.petstore.shared.api.order.models.CreditCardInfo;

import java.util.UUID;

@Builder
public record DebitCreditCardResponse(
        UUID authorizationCode,
        double chargedAmount,
        CreditCardInfo cardInfo
) {
}
