package com.ecom.ecomapp.review;

import java.time.LocalDateTime;

public class ReviewResponse {

    private Long id;
    private Long productId;
    private Long userId;
    private String userName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewResponse(ReviewEntity entity) {
        this.id = entity.getId();
        if (entity.getProduct() != null) {
            this.productId = entity.getProduct().getId();
        }
        if (entity.getUser() != null) {
            this.userId = entity.getUser().getId();
            this.userName = entity.getUser().getName();
        }
        this.rating = entity.getRating();
        this.comment = entity.getComment();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
