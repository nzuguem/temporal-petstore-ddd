package me.nzuguem.petstore.order.domain.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import me.nzuguem.petstore.shared.api.order.models.OrderFailureReason;
import me.nzuguem.petstore.shared.api.order.models.OrderStatus;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
// @Entity
// @Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private UUID id;

    @NotBlank
    // @Column(name = "requested_by", nullable = false)
    private String requestedByUser;

    @NotBlank
    // @Column(name = "requested_by_host", nullable = false)
    private String requestedByHost;

    @NotBlank
    @Email
    // @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @NotNull
    @PastOrPresent
    // @Column(name = "order_date", nullable = false)
    private ZonedDateTime orderDate;

    @NotNull
    // @Column(name = "transaction_id", nullable = false, unique = true)
    private UUID transactionId;

    @NotBlank
    // @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @NotNull
    // @Enumerated(EnumType.STRING)
    // @Column(name = "status", nullable = false)
    private OrderStatus status;

    @NotNull
    // @Column(name="order_total", nullable = false)
    private double orderTotal;

    // @Enumerated(EnumType.STRING)
    //@Column(name = "failure_reason", nullable = false)
    private OrderFailureReason failureReason = OrderFailureReason.NONE;

    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "order_id")
    private Set<OrderLineItem> lineItems;


}