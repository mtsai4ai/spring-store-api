package com.kantares.store.order;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("Order not found");
    }

    public OrderNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
