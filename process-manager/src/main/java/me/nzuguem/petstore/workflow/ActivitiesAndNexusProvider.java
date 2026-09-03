package me.nzuguem.petstore.workflow;

import io.temporal.activity.ActivityCancellationType;
import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.ActivityIdConflictPolicy;
import io.temporal.api.enums.v1.ActivityIdReusePolicy;
import io.temporal.client.StartActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.common.SearchAttributeKey;
import io.temporal.common.SearchAttributes;
import io.temporal.workflow.NexusOperationOptions;
import io.temporal.workflow.NexusServiceOptions;
import io.temporal.workflow.Workflow;
import jakarta.validation.ConstraintViolationException;
import lombok.experimental.UtilityClass;
import me.nzuguem.petstore.shared.api.configurations.ApplicationContextProvider;
import me.nzuguem.petstore.shared.api.inventory.exceptions.OutOfStockException;
import me.nzuguem.petstore.shared.api.inventory.temporal.InventoryActivities;
import me.nzuguem.petstore.shared.api.notification.temporal.OrderNotificationActivities;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentNexusService;
import me.nzuguem.petstore.shared.api.workflow.exceptions.PurchasingException;
import tools.jackson.databind.DatabindException;
import me.nzuguem.petstore.shared.api.order.temporal.OrderServiceActivities;
import me.nzuguem.petstore.shared.api.payment.exceptions.BadPaymentInfoException;
import me.nzuguem.petstore.shared.api.payment.exceptions.PaymentDeclinedException;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentActivities;
import me.nzuguem.petstore.shared.api.shipment.temporal.ShipperActivities;

import java.time.Duration;

@UtilityClass
public class ActivitiesAndNexusProvider {

    private static final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(30))
            .setRetryOptions(RetryOptions.newBuilder()
                    .setDoNotRetry(
                            BadPaymentInfoException.class.getName(),
                            DatabindException.class.getName(),
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
                .setTaskQueue(ApplicationContextProvider.getTemporalQueues().order())
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(OrderServiceActivities.class, newOptions);
    }

    public static OrderNotificationActivities getOrderNotificationActivities() {
        var newOptions = ActivityOptions.newBuilder(options)
                .setTaskQueue(ApplicationContextProvider.getTemporalQueues().notification())
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(OrderNotificationActivities.class, newOptions);
    }

    public static PaymentActivities getPaymentActivities() {
        var newOptions = ActivityOptions.newBuilder(options)
                .setTaskQueue(ApplicationContextProvider.getTemporalQueues().payment())
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(PaymentActivities.class, newOptions);
    }

    public static InventoryActivities getInventoryActivities() {
        var newOptions = ActivityOptions.newBuilder(options)
                .setTaskQueue(ApplicationContextProvider.getTemporalQueues().inventory())
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(InventoryActivities.class, newOptions);
    }

    public static ShipperActivities getShipperActivities() {
        ActivityOptions newOptions = ActivityOptions.newBuilder(options)
                .setTaskQueue(ApplicationContextProvider.getTemporalQueues().shipment())
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        return Workflow.newActivityStub(ShipperActivities.class, newOptions);
    }

    public static PaymentNexusService getPaymentNexusService() {
        return Workflow.newNexusServiceStub(
                PaymentNexusService.class,
                NexusServiceOptions.newBuilder()
                        .setEndpoint(ApplicationContextProvider.getTemporalNexusEndpoints().payment())
                        .setOperationOptions(
                            NexusOperationOptions.newBuilder()
                                    .setScheduleToCloseTimeout(Duration.ofSeconds(30))
                                    .build()
                        )
                        .build()
        );
    }

    public static StartActivityOptions getBaseNotificationsStartActivityOptions(String activityId) {
        return StartActivityOptions.newBuilder()
                .setId(activityId)
                .setTaskQueue(ApplicationContextProvider.getTemporalQueues().notification())
                .setStartToCloseTimeout(Duration.ofSeconds(10))
                .setIdReusePolicy(ActivityIdReusePolicy.ACTIVITY_ID_REUSE_POLICY_ALLOW_DUPLICATE)
                .setIdConflictPolicy(ActivityIdConflictPolicy.ACTIVITY_ID_CONFLICT_POLICY_USE_EXISTING)
                .setTypedSearchAttributes(
                        SearchAttributes.newBuilder()
                                .set(SearchAttributeKey.forKeyword("AppVersion"), ApplicationContextProvider.getApplicationVersion())
                                .build()
                )
                .build();
    }

}
