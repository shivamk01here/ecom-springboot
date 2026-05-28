package com.ecom.ecomapp.wishlist;

import com.ecom.ecomapp.product.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WishlistItemResponse {

    private Long id;
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;
    private LocalDateTime addedAt;

    public WishlistItemResponse(WishlistItemEntity entity) {
        ProductEntity product = entity.getProduct();
        this.id = entity.getId();
        this.productId = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.category = product.getCategory();
        this.addedAt = entity.getAddedAt();
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public java.math.BigDecimal getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    public LocalDateTime getAddedAt() { return addedAt; }
}
