package me.nzuguem.petstore.shared.api.inventory.models;

import lombok.Builder;
import me.nzuguem.petstore.shared.api.order.models.Product;

import java.util.List;

@Builder
public record CheckInventoryRequest(
        List<Product> products
) {
}
