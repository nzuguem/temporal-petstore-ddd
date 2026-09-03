package me.nzuguem.petstore.controllers;


import io.temporal.client.ActivityClient;
import io.temporal.client.StartActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.SearchAttributeKey;
import io.temporal.common.SearchAttributes;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.nzuguem.petstore.controllers.dto.OrderPurchaseRequest;
import me.nzuguem.petstore.controllers.dto.WorkflowInitiationResponse;
import me.nzuguem.petstore.shared.api.configurations.ApplicationContextProvider;
import me.nzuguem.petstore.shared.api.notification.temporal.OrderNotificationActivities;
import me.nzuguem.petstore.shared.api.workflow.temporal.PurchaseOrderWorkflow;
import me.nzuguem.petstore.workflow.ActivitiesAndNexusProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class PurchaseOrderController {

    private final WorkflowClient client;
    private final ActivityClient activityClient;

    public PurchaseOrderController(
            WorkflowClient client,
            ActivityClient activityClient
    ) {
        this.client = client;
        this.activityClient = activityClient;
    }

    @PostMapping
    public ResponseEntity<WorkflowInitiationResponse> purchaseOrder(@RequestBody @Valid OrderPurchaseRequest request) {

        log.info("Initiating order purchase request with incoming request - {}", request);

        try {
            // Create the context
            var ctx = request.toPurchaseOrderContext();

            // Start the workflow
            var workflow = client.newWorkflowStub(PurchaseOrderWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setWorkflowId("OrderPurchase-" + ctx.transactionId())
                            .setTaskQueue(ApplicationContextProvider.getTemporalQueues().purchaseOrder())
                            .setTypedSearchAttributes(
                                    SearchAttributes.newBuilder()
                                            .set(SearchAttributeKey.forKeyword("AppVersion"), ApplicationContextProvider.getApplicationVersion())
                                            .build()
                            )
                            .build()
            );

            WorkflowClient.start(workflow::placeOrder, ctx);

            return ResponseEntity.accepted()
                    .body(WorkflowInitiationResponse.builder()
                            .transactionId(ctx.transactionId())
                            .build());
        } catch (Exception e) {
            log.error("Error processing order purchase request", e);
            throw new RuntimeException("Error processing order purchase request", e);
        }
    }


    @PostMapping("send-order-received-email")
    public ResponseEntity<Map<String, String>> sendOrderReceivedEmail(@RequestBody @Valid OrderPurchaseRequest request) {

        var ctx = request.toPurchaseOrderContext();

        this.activityClient.execute(
                OrderNotificationActivities.class, OrderNotificationActivities::sendOrderReceivedEmail,
                ActivitiesAndNexusProvider.getBaseNotificationsStartActivityOptions(ctx.transactionId().toString()),
                ctx.toOrderReceivedEmailNotificationRequest());

        return ResponseEntity.ok(Map.of("activityId", ctx.transactionId().toString()));
    }

    @PostMapping("send-order-received-email/async")
    public ResponseEntity<Map<String, String>> sendOrderReceivedEmailAsync(@RequestBody @Valid OrderPurchaseRequest request) {
        log.info("🧵🧵🧵 {}", Thread.currentThread());

        var ctx = request.toPurchaseOrderContext();

        this.activityClient.start(
                        OrderNotificationActivities.class, OrderNotificationActivities::sendOrderReceivedEmail,
                        ActivitiesAndNexusProvider.getBaseNotificationsStartActivityOptions(ctx.transactionId().toString()),
                        ctx.toOrderReceivedEmailNotificationRequest())
                .getResultAsync(60, TimeUnit.SECONDS)
                .whenComplete((_, exception) -> {
                    if (exception != null) {
                        log.warn(exception.getMessage(), exception);
                    } else  {
                        log.info("Successfully executed 🧵🧵🧵  {}", Thread.currentThread());
                    }
                });

        return ResponseEntity.accepted()
                .body(Map.of("activityId", ctx.transactionId().toString()));
    }
}
