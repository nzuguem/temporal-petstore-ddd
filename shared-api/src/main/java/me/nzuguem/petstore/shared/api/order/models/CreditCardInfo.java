package me.nzuguem.petstore.shared.api.order.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import me.nzuguem.petstore.shared.api.payment.models.PaymentType;

@Builder
public record CreditCardInfo(

        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "\\d{13,19}", message = "Card number must be between 13 and 19 digits")
        String cardNumber,

        @NotBlank(message = "Card holder name is required")
        String cardHolderName,

        @NotBlank(message = "Expiry date is required")
        @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "Expiry date must be in MM/YY format")
        String expiryDate,

        @NotBlank(message = "CVV is required")
        @Pattern(regexp = "\\d{3,4}", message = "CVV must be 3 or 4 digits")
        String cvv,

        @NotNull(message = "Credit card type is required")
        PaymentType type
) {
}
