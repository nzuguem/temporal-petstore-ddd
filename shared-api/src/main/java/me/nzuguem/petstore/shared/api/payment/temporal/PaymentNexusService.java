package me.nzuguem.petstore.shared.api.payment.temporal;

import io.nexusrpc.Operation;
import io.nexusrpc.Service;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardRequest;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardResponse;
import me.nzuguem.petstore.shared.api.payment.models.ReverseActionsForTransactionRequest;

@Service(name = PaymentNexusService.SERVICE_NAME)
public interface PaymentNexusService {

    String SERVICE_NAME = "payment-service";
    String TASK_QUEUE = "payment-tasks";
    String ENDPOINT = "payment";

    @Operation
    DebitCreditCardResponse debitCreditCard(DebitCreditCardRequest request);

    @Operation
    void reversePaymentTransactions(ReverseActionsForTransactionRequest request);
}
