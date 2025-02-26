package me.nzuguem.petstore.shared.api.payment.exceptions;

public class PaymentDeclinedException extends RuntimeException {

    public PaymentDeclinedException() {
        super();
    }

    public PaymentDeclinedException(String message) {
        super(message);
    }

    public PaymentDeclinedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentDeclinedException(Throwable cause) {
        super(cause);
    }

}