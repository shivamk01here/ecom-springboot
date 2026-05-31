package com.ecom.ecomapp.review;

import com.ecom.ecomapp.product.ProductEntity;
import com.ecom.ecomapp.product.ProductNotFoundException;
import com.ecom.ecomapp.product.ProductRepository;
import com.ecom.ecomapp.user.UserEntity;
import com.ecom.ecomapp.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         ProductRepository productRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<ReviewResponse> getProductReviews(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        return reviewRepository.findByProductId(productId).stream()
                .map(ReviewResponse::new)
                .toList();
    }

    @Transactional
    public ReviewResponse addReview(Long userId, Long productId, ReviewRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        var existing = reviewRepository.findByProductIdAndUserId(productId, userId);
        ReviewEntity review;
        if (existing.isPresent()) {
            review = existing.get();
            review.setRating(request.getRating());
            review.setComment(request.getComment());
        } else {
            review = new ReviewEntity(product, user, request.getRating(), request.getComment());
        }

        return new ReviewResponse(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this review");
        }

        reviewRepository.delete(review);
    }
}
