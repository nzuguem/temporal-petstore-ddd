package me.nzuguem.petstore.payment.application;

import io.nexusrpc.handler.OperationHandler;
import io.nexusrpc.handler.OperationImpl;
import io.nexusrpc.handler.ServiceImpl;
import io.temporal.failure.ApplicationFailure;
import io.temporal.spring.boot.NexusServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.nzuguem.petstore.shared.api.payment.exceptions.PaymentDeclinedException;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardRequest;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardResponse;
import me.nzuguem.petstore.shared.api.payment.models.ReverseActionsForTransactionRequest;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentNexusService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ServiceImpl(service = PaymentNexusService.class)
@NexusServiceImpl(taskQueues = PaymentNexusService.TASK_QUEUE)
public class PaymentNexusApplicationService {

    private final PaymentApplicationService paymentApplicationService;

    public PaymentNexusApplicationService(PaymentApplicationService paymentApplicationService) {
        this.paymentApplicationService = paymentApplicationService;
    }


    @OperationImpl
    public OperationHandler<DebitCreditCardRequest, DebitCreditCardResponse> debitCreditCard() {

        return OperationHandler.sync(
                (ctx, details, input) -> {
                    try {
                        return  this.paymentApplicationService.debitCreditCard(input);
                    } catch (PaymentDeclinedException paymentDeclinedException) {
                        throw ApplicationFailure.newNonRetryableFailureWithCause(
                                paymentDeclinedException.getMessage(),
                                paymentDeclinedException.getClass().getSimpleName(),
                                paymentDeclinedException);
                    }
                }
        );
    }

    @OperationImpl
    public OperationHandler<ReverseActionsForTransactionRequest, Void> reversePaymentTransactions() {

        return OperationHandler.sync(
                (ctx, details, input) -> {
                    this.paymentApplicationService.reversePaymentTransactions(input);
                    return null;
                }
        );
    }
}
