package me.nzuguem.petstore.payment.domain.spi;

import me.nzuguem.petstore.payment.domain.models.Debit;
import me.nzuguem.petstore.shared.api.docs.annotations.Port;

@Port
public interface PaymentOperations {
    void debit(Debit debit);
}
