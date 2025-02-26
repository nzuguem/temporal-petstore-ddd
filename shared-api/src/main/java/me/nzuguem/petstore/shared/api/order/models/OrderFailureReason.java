package me.nzuguem.petstore.shared.api.order.models;

public enum OrderFailureReason {
    NONE("None"),
    INSUFFICIENT_FUNDS("Insufficient funds on one or more payment methods"),
    INVALID_PAYMENT_METHOD("Invalid payment supplied"),
    SYSTEM_ERROR("System error occurred during processing"),
    OUT_OF_STOCK_ITEMS("One or more items were out of stock"),
    PAYMENT_DECLINED("Payment was declined"),
    UNKNOWN("Unknown error");

    private final String description;

    OrderFailureReason(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    @Override
    public String toString() {
        return this.name() + ": " + this.description;
    }
}
