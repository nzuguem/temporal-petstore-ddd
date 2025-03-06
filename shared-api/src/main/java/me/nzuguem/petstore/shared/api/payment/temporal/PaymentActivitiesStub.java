package me.nzuguem.petstore.shared.api.payment.temporal;

import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardRequest;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardResponse;
import me.nzuguem.petstore.shared.api.payment.models.ReverseActionsForTransactionRequest;

import java.util.UUID;

public class PaymentActivitiesStub implements PaymentActivities {

    @Override
    public DebitCreditCardResponse debitCreditCard(DebitCreditCardRequest request) {
        return DebitCreditCardResponse.builder()
                .authorizationCode(UUID.randomUUID())
                .chargedAmount(125d)
                .cardInfo(request.creditCard())
                .build();
    }

    @Override
    public void reversePaymentTransactions(ReverseActionsForTransactionRequest request) {

    }
}
