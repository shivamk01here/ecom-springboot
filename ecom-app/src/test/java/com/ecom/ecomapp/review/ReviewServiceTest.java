package com.ecom.ecomapp.review;

import com.ecom.ecomapp.product.ProductEntity;
import com.ecom.ecomapp.product.ProductNotFoundException;
import com.ecom.ecomapp.product.ProductRepository;
import com.ecom.ecomapp.user.Role;
import com.ecom.ecomapp.user.UserEntity;
import com.ecom.ecomapp.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    private UserEntity user;
    private ProductEntity product;
    private ReviewEntity review;

    @BeforeEach
    void setUp() {
        user = new UserEntity("shivam@example.com", "password", "Shivam", Role.USER);
        user.setId(1L);

        product = new ProductEntity("MacBook", "Apple Laptop", new BigDecimal("1299.99"), "image.jpg", "Electronics", 10);
        product.setId(1L);

        review = new ReviewEntity(product, user, 5, "Amazing product!");
        review.setId(1L);
    }

    @Test
    void getProductReviews_success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.findByProductId(1L)).thenReturn(List.of(review));

        List<ReviewResponse> responses = reviewService.getProductReviews(1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Amazing product!", responses.get(0).getComment());
        assertEquals(5, responses.get(0).getRating());
        verify(productRepository, times(1)).existsById(1L);
        verify(reviewRepository, times(1)).findByProductId(1L);
    }

    @Test
    void getProductReviews_productNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> reviewService.getProductReviews(1L));
        verify(productRepository, times(1)).existsById(1L);
        verify(reviewRepository, never()).findByProductId(anyLong());
    }

    @Test
    void addReview_newReview_success() {
        ReviewRequest request = new ReviewRequest(5, "Brilliant!");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.findByProductIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
        when(reviewRepository.save(any(ReviewEntity.class))).thenAnswer(invocation -> {
            ReviewEntity saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        ReviewResponse response = reviewService.addReview(1L, 1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Brilliant!", response.getComment());
        assertEquals(5, response.getRating());
        verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
    }

    @Test
    void addReview_updateExistingReview_success() {
        ReviewRequest request = new ReviewRequest(4, "Better now!");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.findByProductIdAndUserId(1L, 1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(review);

        ReviewResponse response = reviewService.addReview(1L, 1L, request);

        assertNotNull(response);
        assertEquals("Better now!", review.getComment());
        assertEquals(4, review.getRating());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void deleteReview_success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(1L, 1L);

        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void deleteReview_unauthorized() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reviewService.deleteReview(2L, 1L));
        assertEquals("You are not authorized to delete this review", exception.getMessage());
        verify(reviewRepository, never()).delete(any(ReviewEntity.class));
    }
}
