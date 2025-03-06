package me.nzuguem.petstore.shared.api.order.temporal;

import me.nzuguem.petstore.shared.api.order.models.*;

public class OrderServiceActivitiesStub implements OrderServiceActivities {

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        return CreateOrderResponse.builder()
                .orderTotal(125d)
                .orderNumber("xyz")
                .orderDate(request.orderDate())
                .customerEmail(request.customerEmail())
                .status(OrderStatus.PENDING)
                .transactionId(request.transactionId())
                .build();
    }

    @Override
    public void markOrderAsComplete(MarkOrderCompleteRequest request) {

    }

    @Override
    public void markOrderAsFailed(MarkOrderFailedRequest request) {

    }
}
