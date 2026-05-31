package com.ecom.ecomapp.review;

import com.ecom.ecomapp.config.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductReviews_success() {
        when(reviewService.getProductReviews(1L)).thenReturn(List.of());

        ResponseEntity<List<ReviewResponse>> response = reviewController.getProductReviews(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reviewService, times(1)).getProductReviews(1L);
    }

    @Test
    void addReview_success() {
        UserPrincipal principal = new UserPrincipal(1L, "shivam@example.com", "CUSTOMER");
        ReviewRequest request = new ReviewRequest(5, "Amazing!");
        ReviewResponse mockResponse = mock(ReviewResponse.class);

        when(reviewService.addReview(1L, 1L, request)).thenReturn(mockResponse);

        ResponseEntity<ReviewResponse> response = reviewController.addReview(principal, 1L, request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(reviewService, times(1)).addReview(1L, 1L, request);
    }

    @Test
    void deleteReview_success() {
        UserPrincipal principal = new UserPrincipal(1L, "shivam@example.com", "CUSTOMER");

        ResponseEntity<Void> response = reviewController.deleteReview(principal, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reviewService, times(1)).deleteReview(1L, 1L);
    }
}
