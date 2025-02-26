package me.nzuguem.petstore.shared.api.payment.exceptions;

public class BadPaymentInfoException extends RuntimeException {

    public BadPaymentInfoException() {
    }

    public BadPaymentInfoException(String message) {
        super(message);
    }

    public BadPaymentInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadPaymentInfoException(Throwable cause) {
        super(cause);
    }

    public BadPaymentInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
