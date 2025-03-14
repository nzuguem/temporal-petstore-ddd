package me.nzuguem.petstore.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import me.nzuguem.petstore.BaseE2ETests;
import me.nzuguem.petstore.shared.api.configurations.ApplicationContextProvider;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentNexusService;
import me.nzuguem.petstore.shared.api.workflow.temporal.PurchaseOrderWorkflow;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

abstract class BaseE2EWorkflowTests extends BaseE2ETests {

    @Autowired
    protected ConfigurableApplicationContext applicationContext;

    @Autowired
    protected WorkflowClient workflowClient;

    protected PurchaseOrderWorkflow purchaseOrderWorkflow;

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        this.applicationContext.start();
        this.purchaseOrderWorkflow = this.workflowClient.newWorkflowStub(PurchaseOrderWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId("OrderPurchase-" + UUID.randomUUID())
                        .setTaskQueue(ApplicationContextProvider.getTemporalQueues().purchaseOrder())
                        .build()
        );
    }
}
