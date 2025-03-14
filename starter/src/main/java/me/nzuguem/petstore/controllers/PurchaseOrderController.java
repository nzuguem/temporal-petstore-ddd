package me.nzuguem.petstore.controllers;


import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.nzuguem.petstore.configurations.filters.RequestIdFilters;
import me.nzuguem.petstore.controllers.dto.OrderPurchaseRequest;
import me.nzuguem.petstore.controllers.dto.WorkflowInitiationResponse;
import me.nzuguem.petstore.shared.api.configurations.ApplicationContextProvider;
import me.nzuguem.petstore.shared.api.workflow.models.PurchaseOrderContext;
import me.nzuguem.petstore.shared.api.workflow.temporal.PurchaseOrderWorkflow;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@RestController
public class PurchaseOrderController {

    private final WorkflowClient client;

    public PurchaseOrderController(WorkflowClient client) {
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<WorkflowInitiationResponse> purchaseOrder(@RequestBody @Valid OrderPurchaseRequest request) {

        log.info("Initiating order purchase request with incoming request - {}", request);

        try {
            // Get the transaction id from the request
            var requestId = UUID.fromString(MDC.get(RequestIdFilters.REQUEST_ID_MDC_KEY));

            // Start the workflow
            var workflow = client.newWorkflowStub(PurchaseOrderWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setWorkflowId("OrderPurchase-" + requestId)
                            .setTaskQueue(ApplicationContextProvider.getTemporalQueues().purchaseOrder())
                            .build()
            );

            // Create the context
            var ctx = PurchaseOrderContext.builder()
                        .transactionId(requestId)
                        .customerEmail(request.customerEmail())
                        .creditCard(request.creditCard())
                        .products(request.products())
                        .requestDate(ZonedDateTime.now())
                        .requestedByHost(MDC.get(RequestIdFilters.REQUEST_IP_MDC_KEY))
                        .requestedByUser(MDC.get(RequestIdFilters.REQUEST_USER_MDC_KEY))
                        .build();

            WorkflowClient.start(workflow::placeOrder, ctx);
            return ResponseEntity.accepted()
                    .body(WorkflowInitiationResponse.builder()
                            .transactionId(requestId)
                            .build());
        } catch (Exception e) {
            log.error("Error processing order purchase request", e);
            throw new RuntimeException("Error processing order purchase request", e);
        }
    }
}
