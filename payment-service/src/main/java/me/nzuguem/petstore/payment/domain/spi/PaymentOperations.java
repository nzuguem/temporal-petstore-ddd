package me.nzuguem.petstore.payment.domain.spi;

import me.nzuguem.petstore.payment.domain.models.Debit;

public interface PaymentOperations {
    void debit(Debit debit);
}
