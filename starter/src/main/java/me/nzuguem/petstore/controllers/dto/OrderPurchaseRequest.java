package me.nzuguem.petstore.controllers.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import me.nzuguem.petstore.shared.api.order.models.CreditCardInfo;
import me.nzuguem.petstore.shared.api.order.models.Product;

import java.util.List;

@Builder
public record OrderPurchaseRequest(
        @NotNull(message = "Credit card information is required")
        @Valid
        CreditCardInfo creditCard,

        @NotBlank(message = "Customer email is required")
        @Email(message = "Email should be valid")
        String customerEmail,

        @NotNull(message = "Product list cannot be null")
        List<@NotNull(message = "Product cannot be null") @Valid Product> products
) {
}
