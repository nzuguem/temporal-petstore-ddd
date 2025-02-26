package me.nzuguem.petstore.shared.api.order.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record Product(

        @NotBlank(message = "Product SKU is required")
        String sku,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @Min(value = 1, message = "Price must be at least 1")
        double price
) {
}
