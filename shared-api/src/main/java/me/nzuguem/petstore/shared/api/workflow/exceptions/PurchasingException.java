package me.nzuguem.petstore.shared.api.workflow.exceptions;

public class PurchasingException extends RuntimeException {

    public PurchasingException() {
    }

    public PurchasingException(String message) {
        super(message);
    }

    public PurchasingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PurchasingException(Throwable cause) {
        super(cause);
    }

    public PurchasingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
