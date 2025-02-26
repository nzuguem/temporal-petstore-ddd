package me.nzuguem.petstore.payment.domain.models;

import lombok.Builder;
import me.nzuguem.petstore.shared.api.payment.exceptions.BadPaymentInfoException;
import me.nzuguem.petstore.shared.api.payment.exceptions.PaymentDeclinedException;

@Builder
public record Debit(
        String customerEmail,
        double amount
) {

    private static final int CREDIT_CARD_DEBIT_LIMIT = 1000;

    public Debit {

        if ("bad_customer@foo.com".equalsIgnoreCase(customerEmail)) {
            throw new BadPaymentInfoException("Customer email doesn't match card owner");
        } else if (amount > CREDIT_CARD_DEBIT_LIMIT) {
            throw new PaymentDeclinedException(
                    "Order amount " + amount+ " exceeds credit limit of " + CREDIT_CARD_DEBIT_LIMIT);
        }

    }
}
