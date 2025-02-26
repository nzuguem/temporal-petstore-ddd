package me.nzuguem.petstore.payment.application;

import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import me.nzuguem.petstore.payment.domain.models.Debit;
import me.nzuguem.petstore.payment.domain.services.PaymentService;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardRequest;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardResponse;
import me.nzuguem.petstore.shared.api.payment.models.ReverseActionsForTransactionRequest;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentActivities;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@ActivityImpl(taskQueues = PaymentActivities.TASK_QUEUE)
public class PaymentApplicationService implements PaymentActivities {

    private final PaymentService paymentService;

    public PaymentApplicationService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public DebitCreditCardResponse debitCreditCard(DebitCreditCardRequest request) {

        Objects.requireNonNull(request, "DebitCreditCardRequest instance required");

        log.info("Attempting to debit {} from credit card {}", request.amount(),
                request.creditCard().cardNumber());

       this.paymentService.debitCreditCard(Debit.builder()
               .customerEmail(request.customerEmail())
               .amount(request.amount())
               .build()
       );

        // Return the response
        return DebitCreditCardResponse.builder()
                .authorizationCode(UUID.randomUUID())
                .cardInfo(request.creditCard())
                .chargedAmount(request.amount())
                .build();
    }

    @Override
    public void reversePaymentTransactions(ReverseActionsForTransactionRequest request) {

        Objects.requireNonNull(request, "ReverseAccountsTransactionRequest instance required");

        log.info("Attempting compensations for all transactions with TX id {}", request.transactionId());

        log.info("Compensation completed for all transactions with TX id {}", request.transactionId());
    }
}
