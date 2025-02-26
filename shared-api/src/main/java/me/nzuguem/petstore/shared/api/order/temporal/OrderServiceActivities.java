package me.nzuguem.petstore.shared.api.order.temporal;

import io.temporal.activity.ActivityInterface;
import me.nzuguem.petstore.shared.api.order.models.CreateOrderRequest;
import me.nzuguem.petstore.shared.api.order.models.CreateOrderResponse;
import me.nzuguem.petstore.shared.api.order.models.MarkOrderCompleteRequest;
import me.nzuguem.petstore.shared.api.order.models.MarkOrderFailedRequest;

@ActivityInterface
public interface OrderServiceActivities {

    String TASK_QUEUE = "order-tasks";

    CreateOrderResponse createOrder(CreateOrderRequest request);
    void markOrderAsComplete(MarkOrderCompleteRequest request);
    void markOrderAsFailed(MarkOrderFailedRequest request);
}