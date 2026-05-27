package com.ecom.ecomapp.cart;

import java.math.BigDecimal;

public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private String imageUrl;
    private int quantity;
    private BigDecimal subtotal;

    public CartItemResponse(CartItemEntity entity) {
        this.id = entity.getId();
        this.productId = entity.getProduct().getId();
        this.productName = entity.getProduct().getName();
        this.price = entity.getProduct().getPrice();
        this.imageUrl = entity.getProduct().getImageUrl();
        this.quantity = entity.getQuantity();
        this.subtotal = entity.getProduct().getPrice().multiply(BigDecimal.valueOf(entity.getQuantity()));
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
}
