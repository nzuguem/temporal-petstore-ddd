package me.nzuguem.petstore.workflow;

import com.fasterxml.jackson.databind.JsonMappingException;
import io.temporal.activity.ActivityCancellationType;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import jakarta.validation.ConstraintViolationException;
import lombok.experimental.UtilityClass;
import me.nzuguem.petstore.shared.api.inventory.exceptions.OutOfStockException;
import me.nzuguem.petstore.shared.api.inventory.temporal.InventoryActivities;
import me.nzuguem.petstore.shared.api.notification.temporal.OrderNotificationActivities;
import me.nzuguem.petstore.shared.api.workflow.exceptions.PurchasingException;
import me.nzuguem.petstore.shared.api.order.temporal.OrderServiceActivities;
import me.nzuguem.petstore.shared.api.payment.exceptions.BadPaymentInfoException;
import me.nzuguem.petstore.shared.api.payment.exceptions.PaymentDeclinedException;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentActivities;
import me.nzuguem.petstore.shared.api.shipment.temporal.ShipperActivities;

import java.time.Duration;

@UtilityClass
public class ActivitiesProvider {

    private final static ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(30))
            .setRetryOptions(RetryOptions.newBuilder()
                    .setDoNotRetry(
                            BadPaymentInfoException.class.getName(),
                            JsonMappingException.class.getName(),
                            ConstraintViolationException.class.getName(),
                            NullPointerException.class.getName(),
                            OutOfStockException.class.getName(),
                            PaymentDeclinedException.class.getName(),
                            PurchasingException.class.getName(),
                            IllegalArgumentException.class.getName())
                    .setInitialInterval(Duration.ofSeconds(1))
                    .setMaximumInterval(Duration.ofSeconds(100))
                    .setBackoffCoefficient(2)
                    .setMaximumAttempts(500)
                    .build())
            .build();

    public static OrderServiceActivities getOrderServiceActivities() {
        var newOptions = ActivityOptions.newBuilder(options)
                .setTaskQueue(OrderServiceActivities.TASK_QUEUE)
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(OrderServiceActivities.class, newOptions);
    }

    public static OrderNotificationActivities getOrderNotificationActivities() {
        var newOptions = ActivityOptions.newBuilder(options)
                .setTaskQueue(OrderNotificationActivities.TASK_QUEUE)
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(OrderNotificationActivities.class, newOptions);
    }

    public static PaymentActivities getPaymentActivities() {
        var newOptions = ActivityOptions.newBuilder(options)
                .setTaskQueue(PaymentActivities.TASK_QUEUE)
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(PaymentActivities.class, newOptions);
    }

    public static InventoryActivities getInventoryActivities() {
        var newOptions = ActivityOptions.newBuilder(options)
                .setTaskQueue(InventoryActivities.TASK_QUEUE)
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(InventoryActivities.class, newOptions);
    }

    public static ShipperActivities getShipperActivities() {
        ActivityOptions newOptions = ActivityOptions.newBuilder(options)
                .setTaskQueue(ShipperActivities.TASK_QUEUE)
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(ShipperActivities.class, newOptions);
    }

}
