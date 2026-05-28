package com.ecom.ecomapp.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private String imageUrl;

    public OrderItemResponse(OrderItemEntity entity) {
        this.id = entity.getId();
        this.productId = entity.getProduct().getId();
        this.productName = entity.getProduct().getName();
        this.quantity = entity.getQuantity();
        this.price = entity.getPrice();
        this.subtotal = entity.getSubtotal();
        this.imageUrl = entity.getProduct().getImageUrl();
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getSubtotal() { return subtotal; }
    public String getImageUrl() { return imageUrl; }
}
