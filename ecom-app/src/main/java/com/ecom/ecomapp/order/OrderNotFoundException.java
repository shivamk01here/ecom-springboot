package com.ecom.ecomapp.order;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super("Order with ID " + orderId + " not found");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
