package me.nzuguem.petstore.order.application;

import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import me.nzuguem.petstore.shared.api.order.models.*;
import me.nzuguem.petstore.shared.api.order.temporal.OrderServiceActivities;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@ActivityImpl(taskQueues = OrderServiceActivities.TASK_QUEUE)
public class OrderApplicationService implements OrderServiceActivities {

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for TX id {}", request.transactionId());

        return CreateOrderResponse.builder()
                .transactionId(request.transactionId())
                .orderDate(request.orderDate())
                .customerEmail(request.customerEmail())
                .orderNumber(this.generateOrderNumber())
                .status(OrderStatus.PENDING)
                .orderTotal(calculateTotalPrice(request.products()))
                .build();
    }

    @Override
    public void markOrderAsComplete(MarkOrderCompleteRequest request) {
        log.info("Marking order as complete for TX id {}", request.transactionId());
    }

    @Override
    public void markOrderAsFailed(MarkOrderFailedRequest request) {
        log.info("Marking order as failed for TX id {}", request.transactionId());
    }

    private String generateOrderNumber() {

        // Generate a UUID
        var uuid = UUID.randomUUID();

        // Convert UUID to a string and remove the hyphens
        String uuidString = uuid.toString().replace("-", "");

        // Extract the first 16 hexadecimal digits
        String first16HexDigits = uuidString.substring(0, 16);

        // Format the string to match RJT-xxxxxxxx-xxxx-xxxx
        String formattedOrderNumber = String.format("PET-ORD-%s-%s-%s",
                first16HexDigits.substring(0, 8),
                first16HexDigits.substring(8, 12),
                first16HexDigits.substring(12, 16));

        log.info("Generated a new order number: {}", formattedOrderNumber);
        return formattedOrderNumber;
    }

    private double calculateTotalPrice(List<Product> products) {
        return products.stream()
                    .mapToDouble(product -> product.quantity() * product.price())
                    .sum();
    }
}
