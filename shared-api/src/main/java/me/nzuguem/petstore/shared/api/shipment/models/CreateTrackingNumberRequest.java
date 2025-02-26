package me.nzuguem.petstore.shared.api.shipment.models;

import lombok.Builder;
import me.nzuguem.petstore.shared.api.order.models.Product;

import java.util.List;

@Builder
public record CreateTrackingNumberRequest(
        List<Product> products
) {
}
