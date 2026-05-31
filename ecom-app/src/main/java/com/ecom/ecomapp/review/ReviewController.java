package com.ecom.ecomapp.review;

import com.ecom.ecomapp.config.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<ReviewResponse> addReview(@AuthenticationPrincipal UserPrincipal principal,
                                                    @PathVariable Long productId,
                                                    @Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.addReview(principal.id(), productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal UserPrincipal principal,
                                             @PathVariable Long reviewId) {
        reviewService.deleteReview(principal.id(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
