package me.nzuguem.petstore.payment.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
import me.nzuguem.petstore.payment.domain.models.Debit;
import me.nzuguem.petstore.payment.domain.spi.PaymentOperations;
import me.nzuguem.petstore.shared.api.docs.annotations.Adapter;

@Slf4j
@Adapter
public class PaymentOperationsAdapter implements PaymentOperations {

    @Override
    public void debit(Debit debit) {
        log.info("Debiting credit card {}", debit);
    }
}
