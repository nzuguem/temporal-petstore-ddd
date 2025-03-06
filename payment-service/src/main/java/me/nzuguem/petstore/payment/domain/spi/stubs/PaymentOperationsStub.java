package me.nzuguem.petstore.payment.domain.spi.stubs;

import me.nzuguem.petstore.payment.domain.models.Debit;
import me.nzuguem.petstore.payment.domain.spi.PaymentOperations;
import me.nzuguem.petstore.shared.api.docs.annotations.Stub;

@Stub
public class PaymentOperationsStub implements PaymentOperations {

    @Override
    public void debit(Debit debit) {
    }
}
