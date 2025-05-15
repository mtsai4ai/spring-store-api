package com.kantares.store.payments;

public class PaymentException extends RuntimeException {
    public PaymentException() {
        super("Payment processing error");
    }

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
