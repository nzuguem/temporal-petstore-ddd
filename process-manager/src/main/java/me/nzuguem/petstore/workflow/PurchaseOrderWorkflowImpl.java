package me.nzuguem.petstore.workflow;

import io.temporal.failure.CanceledFailure;
import io.temporal.failure.TemporalFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import me.nzuguem.petstore.shared.api.inventory.exceptions.OutOfStockException;
import me.nzuguem.petstore.shared.api.inventory.temporal.InventoryActivities;
import me.nzuguem.petstore.shared.api.notification.temporal.OrderNotificationActivities;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentNexusService;
import me.nzuguem.petstore.shared.api.workflow.models.PurchaseOrderContext;
import me.nzuguem.petstore.shared.api.workflow.temporal.PurchaseOrderWorkflow;
import me.nzuguem.petstore.shared.api.order.models.MarkOrderFailedRequest;
import me.nzuguem.petstore.shared.api.order.models.OrderFailureReason;
import me.nzuguem.petstore.shared.api.order.temporal.OrderServiceActivities;
import me.nzuguem.petstore.shared.api.payment.exceptions.BadPaymentInfoException;
import me.nzuguem.petstore.shared.api.payment.exceptions.PaymentDeclinedException;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentActivities;
import me.nzuguem.petstore.shared.api.shipment.temporal.ShipperActivities;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.UUID;

@WorkflowImpl(taskQueues = "${app.temporal.task-queues.purchase-order}")
public class PurchaseOrderWorkflowImpl implements PurchaseOrderWorkflow {

    private static final Logger log = Workflow.getLogger(PurchaseOrderWorkflowImpl.class);

    private final OrderNotificationActivities orderNotificationActivities = ActivitiesAndNexusProvider.getOrderNotificationActivities();
    private final OrderServiceActivities orderServiceActivities = ActivitiesAndNexusProvider.getOrderServiceActivities();
    private final PaymentActivities paymentActivities = ActivitiesAndNexusProvider.getPaymentActivities();
    private final PaymentNexusService paymentNexusService = ActivitiesAndNexusProvider.getPaymentNexusService();
    private final InventoryActivities inventoryActivities = ActivitiesAndNexusProvider.getInventoryActivities();
    private final ShipperActivities shipperActivities = ActivitiesAndNexusProvider.getShipperActivities();

    @Override
    public void placeOrder(PurchaseOrderContext ctx) {

        Objects.requireNonNull(ctx, "PurchaseOrderContext is required");

        // Initialize the saga for potential compensations
        var saga = new Saga(new Saga.Options.Builder().build());

        try {
            // 1. Send the acknowledgement of the order request
            this.sendOrderReceivedEmail(ctx);

            // 2. Create the initial order record
            ctx = this.generateProductOrder(ctx);

            // 3. Charge the credit card
            this.debitCreditCard(saga, ctx);

            // 4. Check with warehouse to see if the products are in stock - fail if not
            this.inventoryActivities.checkInventory(ctx.toCheckInventoryRequest());

            // 5. get the shipping information/tracking number from the shipper
            ctx = this.createTrackingNumber(ctx);

            // 6. Save order history and send out email
            this.completeOrder(ctx);

        } catch (TemporalFailure failure) {
            log.error(failure.getOriginalMessage(), failure);
            var finalCtx = ctx;
            Workflow.newDetachedCancellationScope(
                    () -> cleanup(failure, saga, finalCtx, finalCtx.transactionId())).run();
            throw failure;
        }
    }

    private void sendOrderReceivedEmail(PurchaseOrderContext ctx) {
        log.info("Sending order request received notification");
        this.orderNotificationActivities.sendOrderReceivedEmail(ctx.toOrderReceivedEmailNotificationRequest());
    }

    private PurchaseOrderContext generateProductOrder(PurchaseOrderContext ctx) {
        log.info("Generating new order record for TX id {}", ctx.transactionId());

        var orderResponse =this.orderServiceActivities.createOrder(ctx.toCreateOrderRequest());

        return  ctx.toBuilder()
                    .orderNumber(orderResponse.orderNumber())
                    .status(orderResponse.status())
                    .orderTotal(orderResponse.orderTotal())
                    .build();
    }

    private void cleanup(Exception e, Saga saga, PurchaseOrderContext ctx, UUID transactionId) {
        log.info("Performing cleanup operations for TX id {}", transactionId);

        try {
            if (saga != null) {
                saga.compensate();
            }
        } catch (Exception cpe) {
            log.error("Failed to complete compensations!", cpe);
        }

        if (!(e instanceof CanceledFailure)) {
            if (ctx != null) {
                failOrder(e, ctx);
            }
        }

        log.info("Finished cleanup operations for TX id {}", transactionId);
    }

    private void failOrder(Exception e, PurchaseOrderContext ctx) {

        // Default to SYSTEM error
        var reason = OrderFailureReason.SYSTEM_ERROR;

        // Check for specific errors
        if (ExceptionsChecker.isExceptionType(e, PaymentDeclinedException.class)) {
            reason = OrderFailureReason.PAYMENT_DECLINED;
        } else if (ExceptionsChecker.isExceptionType(e, BadPaymentInfoException.class)) {
            reason = OrderFailureReason.INVALID_PAYMENT_METHOD;
        } else if (ExceptionsChecker.isExceptionType(e, OutOfStockException.class)) {
            reason = OrderFailureReason.OUT_OF_STOCK_ITEMS;
        }

        log.info("Marking order as failed with TX id {}", ctx.transactionId());

        var req = MarkOrderFailedRequest.builder()
                .orderNumber(ctx.orderNumber())
                .transactionId(ctx.transactionId())
                .reason(reason)
                .build();

        this.orderServiceActivities.markOrderAsFailed(req);
        this.orderNotificationActivities.sendOrderErrorEmail(ctx.toOrderErrorEmailNotificationRequest());
    }

    private void debitCreditCard(Saga saga, PurchaseOrderContext ctx) {

        log.info("Calling Payment service to debit credit card");

        // Create the reversal in case of compensations later
        saga.addCompensation(() -> this.paymentActivities.reversePaymentTransactions(ctx.toReverseActionsForTransactionRequest()));
        // debit card and return some sort of auth number or whatever
        this.paymentNexusService.debitCreditCard(ctx.toDebitCreditCardRequest());

    }

    private PurchaseOrderContext createTrackingNumber(PurchaseOrderContext ctx) {
        var trackingNumber = this.shipperActivities.createTrackingNumber(ctx.toCreateTrackingNumberRequest());
        return ctx.toBuilder()
                .trackingNumber(trackingNumber)
                .build();
    }

    private void completeOrder(PurchaseOrderContext ctx) {

        log.info("Marking order %s as complete with TX id {}", ctx.orderNumber(), ctx.transactionId());

        this.orderServiceActivities.markOrderAsComplete(ctx.toMarkOrderCompleteRequest());

        log.info("Order updated..Sending notification email to {}", ctx.customerEmail());
        this.orderNotificationActivities.sendOrderSuccessEmail(ctx.toOrderSuccessEmailNotificationRequest());
    }
}
