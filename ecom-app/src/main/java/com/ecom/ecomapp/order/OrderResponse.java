package com.ecom.ecomapp.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String billingAddress;
    private String phoneNumber;
    private String notes;
    private List<OrderItemResponse> items;

    public OrderResponse(OrderEntity entity) {
        this.id = entity.getId();
        this.orderDate = entity.getOrderDate();
        this.status = entity.getStatus();
        this.totalAmount = entity.getTotalAmount();
        this.shippingAddress = entity.getShippingAddress();
        this.billingAddress = entity.getBillingAddress();
        this.phoneNumber = entity.getPhoneNumber();
        this.notes = entity.getNotes();
        this.items = entity.getItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getShippingAddress() { return shippingAddress; }
    public String getBillingAddress() { return billingAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getNotes() { return notes; }
    public List<OrderItemResponse> getItems() { return items; }
}
