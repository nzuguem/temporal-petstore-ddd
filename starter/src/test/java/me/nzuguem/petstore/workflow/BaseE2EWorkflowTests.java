package me.nzuguem.petstore.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import me.nzuguem.petstore.BaseE2ETests;
import me.nzuguem.petstore.shared.api.workflow.temporal.PurchaseOrderWorkflow;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

abstract class BaseE2EWorkflowTests extends BaseE2ETests {

    @Autowired
    protected ConfigurableApplicationContext applicationContext;

    @Autowired
    protected TestWorkflowEnvironment testWorkflowEnvironment;

    @Autowired
    protected WorkflowClient workflowClient;

    protected PurchaseOrderWorkflow purchaseOrderWorkflow;

    @BeforeEach
    void setUp() {
        this.applicationContext.start();
        this.purchaseOrderWorkflow = this.workflowClient.newWorkflowStub(PurchaseOrderWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId("OrderPurchase-" + UUID.randomUUID())
                        .setTaskQueue(PurchaseOrderWorkflow.TASK_QUEUE).build());
    }
}
