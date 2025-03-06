package me.nzuguem.petstore.shared.api.workflow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.nzuguem.petstore.shared.api.inventory.models.CheckInventoryRequest;
import me.nzuguem.petstore.shared.api.notification.models.OrderErrorEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.notification.models.OrderReceivedEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.notification.models.OrderSuccessEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.order.models.*;
import me.nzuguem.petstore.shared.api.payment.models.DebitCreditCardRequest;
import me.nzuguem.petstore.shared.api.payment.models.ReverseActionsForTransactionRequest;
import me.nzuguem.petstore.shared.api.shipment.models.CreateTrackingNumberRequest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class PurchaseOrderContext {

    private String requestedByUser;
    private String requestedByHost;
    private UUID transactionId;
    private ZonedDateTime requestDate;
    private OrderStatus status;
    private CreditCardInfo creditCard;
    private String customerEmail;
    private List<Product> products;
    private String orderNumber;
    private String trackingNumber;
    private double orderTotal;

    public String requestedByUser() {
        return requestedByUser;
    }

    public String requestedByHost() {
        return requestedByHost;
    }

    public UUID transactionId() {
        return transactionId;
    }

    public ZonedDateTime requestDate() {
        return requestDate;
    }

    public OrderStatus status() {
        return status;
    }

    public CreditCardInfo creditCard() {
        return creditCard;
    }

    public String customerEmail() {
        return customerEmail;
    }

    public List<Product> products() {
        return products;
    }

    public String orderNumber() {
        return orderNumber;
    }

    public String trackingNumber() {
        return trackingNumber;
    }

    public double orderTotal() {
        return orderTotal;
    }

    public OrderReceivedEmailNotificationRequest toOrderReceivedEmailNotificationRequest() {
        return OrderReceivedEmailNotificationRequest.builder()
                .transactionNumber(transactionId)
                .customerEmail(customerEmail)
                .orderDate(requestDate)
                .products(products)
                .build();
    }

    public OrderErrorEmailNotificationRequest toOrderErrorEmailNotificationRequest() {
        return OrderErrorEmailNotificationRequest.builder()
                .orderDate(requestDate)
                .customerEmail(customerEmail)
                .orderNumber(orderNumber)
                .transactionNumber(transactionId)
                .build();
    }

    public CreateOrderRequest toCreateOrderRequest() {
        return CreateOrderRequest.builder()
                .requestedByUser(requestedByUser)
                .requestedByHost(requestedByHost)
                .customerEmail(customerEmail)
                .orderDate(requestDate)
                .transactionId(transactionId)
                .products(products)
                .build();
    }

    public ReverseActionsForTransactionRequest toReverseActionsForTransactionRequest() {
        return ReverseActionsForTransactionRequest.builder()
                .requestedByHost(requestedByHost)
                .requestedByUser(requestedByUser)
                .transactionId(transactionId)
                .build();
    }

    public DebitCreditCardRequest toDebitCreditCardRequest() {
        return DebitCreditCardRequest.builder()
                .amount(orderTotal)
                .creditCard(creditCard)
                .customerEmail(customerEmail)
                .transactionId(transactionId)
                .requestedByHost(requestedByHost)
                .requestedByUser(requestedByUser)
                .build();
    }

    public CheckInventoryRequest toCheckInventoryRequest() {
        return CheckInventoryRequest.builder()
                .products(products)
                .build();
    }

    public CreateTrackingNumberRequest toCreateTrackingNumberRequest() {
        return CreateTrackingNumberRequest.builder()
                .products(products)
                .build();
    }

    public MarkOrderCompleteRequest toMarkOrderCompleteRequest() {
        return MarkOrderCompleteRequest.builder()
                .products(products)
                .orderDate(requestDate)
                .orderNumber(orderNumber)
                .transactionId(transactionId)
                .customerEmail(customerEmail)
                .orderTotal(orderTotal)
                .build();
    }

    public OrderSuccessEmailNotificationRequest toOrderSuccessEmailNotificationRequest() {
        return OrderSuccessEmailNotificationRequest.builder()
                .customerEmail(customerEmail)
                .transactionNumber(transactionId)
                .orderNumber(orderNumber)
                .orderDate(requestDate)
                .products(products)
                .trackingNumber(trackingNumber)
                .orderTotal(orderTotal)
                .build();
    }

    @Override
    public String toString() {
        return "PurchaseOrderContext[" +
                "requestedByUser=" + requestedByUser + ", " +
                "requestedByHost=" + requestedByHost + ", " +
                "transactionId=" + transactionId + ", " +
                "requestDate=" + requestDate + ", " +
                "status=" + status + ", " +
                "creditCard=" + creditCard + ", " +
                "customerEmail=" + customerEmail + ", " +
                "products=" + products + ", " +
                "orderNumber=" + orderNumber + ", " +
                "trackingNumber=" + trackingNumber + ", " +
                "orderTotal=" + orderTotal + ']';
    }

}
