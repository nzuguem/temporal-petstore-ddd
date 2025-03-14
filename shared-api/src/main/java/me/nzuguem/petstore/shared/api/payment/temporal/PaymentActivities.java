package me.nzuguem.petstore.shared.api.payment.temporal;

import io.temporal.activity.ActivityInterface;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardRequest;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardResponse;
import me.nzuguem.petstore.shared.api.payment.models.ReverseActionsForTransactionRequest;

@ActivityInterface
public interface PaymentActivities {
    DebitCreditCardResponse debitCreditCard(DebitCreditCardRequest request);
    void reversePaymentTransactions(ReverseActionsForTransactionRequest request);
}
