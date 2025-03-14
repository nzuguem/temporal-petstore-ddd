package me.nzuguem.petstore.shared.api.workflow.temporal;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import me.nzuguem.petstore.shared.api.workflow.models.PurchaseOrderContext;


@WorkflowInterface
public interface PurchaseOrderWorkflow {
    @WorkflowMethod(name = "placeOrder")
    void placeOrder(PurchaseOrderContext ctx);
}

