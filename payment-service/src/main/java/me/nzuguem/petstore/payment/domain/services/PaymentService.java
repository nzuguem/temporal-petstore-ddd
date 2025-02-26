package me.nzuguem.petstore.payment.domain.services;

import me.nzuguem.petstore.payment.domain.models.Debit;
import me.nzuguem.petstore.payment.domain.spi.PaymentOperations;
import me.nzuguem.petstore.shared.api.annotations.DomainService;

import java.util.Objects;

@DomainService
public class PaymentService {

    private final PaymentOperations paymentOperations;

    public PaymentService(PaymentOperations paymentOperations) {
        this.paymentOperations = paymentOperations;
    }

    public void debitCreditCard(Debit debit) {

        Objects.requireNonNull(debit, "Debit instance required");

        this.paymentOperations.debit(debit);
    }
}
