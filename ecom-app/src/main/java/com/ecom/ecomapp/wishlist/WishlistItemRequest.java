package com.ecom.ecomapp.wishlist;

import jakarta.validation.constraints.NotNull;

public class WishlistItemRequest {

    @NotNull
    private Long productId;

    public WishlistItemRequest() {}

    public WishlistItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}
