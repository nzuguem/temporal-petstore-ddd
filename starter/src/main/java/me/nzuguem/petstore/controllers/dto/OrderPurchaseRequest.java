package me.nzuguem.petstore.controllers.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import me.nzuguem.petstore.configurations.filters.RequestIdFilters;
import me.nzuguem.petstore.shared.api.order.models.CreditCardInfo;
import me.nzuguem.petstore.shared.api.order.models.Product;
import me.nzuguem.petstore.shared.api.workflow.models.PurchaseOrderContext;
import org.slf4j.MDC;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

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

        public PurchaseOrderContext toPurchaseOrderContext() {
                return PurchaseOrderContext.builder()
                        .transactionId(UUID.fromString(MDC.get(RequestIdFilters.REQUEST_ID_MDC_KEY)))
                        .customerEmail(this.customerEmail())
                        .creditCard(this.creditCard())
                        .products(this.products())
                        .requestDate(ZonedDateTime.now())
                        .requestedByHost(MDC.get(RequestIdFilters.REQUEST_IP_MDC_KEY))
                        .requestedByUser(MDC.get(RequestIdFilters.REQUEST_USER_MDC_KEY))
                        .build();
        }
}
