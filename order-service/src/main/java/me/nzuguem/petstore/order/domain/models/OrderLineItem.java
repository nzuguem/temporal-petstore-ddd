package me.nzuguem.petstore.order.domain.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Builder
// @Entity
// @Table(name = "order_line_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItem {

    // @Id
    // @GeneratedValue
    private UUID id;

    @NotNull
    // @Column(name = "quantity", nullable = false)
    private int quantity;

    @NotNull
    // @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @NotBlank
    // @Column(name = "product_sku", nullable = false)
    private String productSku;

}
