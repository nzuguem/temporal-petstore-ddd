package me.nzuguem.petstore.shared.api.order.models;

public enum OrderStatus {

    COMPLETED("Order was completed"),

    CANCELLED("Order was cancelled"),

    PENDING("Order is created and awaiting completion"),

    FAILED("An error occurred during order processing");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
