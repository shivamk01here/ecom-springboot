package com.ecom.ecomapp.coupon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    private CouponEntity activeCoupon;
    private CouponEntity inactiveCoupon;
    private CouponEntity expiredCoupon;

    @BeforeEach
    void setUp() {
        activeCoupon = new CouponEntity("SAVE10", BigDecimal.valueOf(10.00), LocalDateTime.now().plusDays(5), true);
        inactiveCoupon = new CouponEntity("SAVE20", BigDecimal.valueOf(20.00), LocalDateTime.now().plusDays(5), false);
        expiredCoupon = new CouponEntity("SAVE30", BigDecimal.valueOf(30.00), LocalDateTime.now().minusDays(5), true);
    }

    @Test
    void validateCoupon_success() {
        when(couponRepository.findByCodeIgnoreCase("SAVE10")).thenReturn(Optional.of(activeCoupon));

        CouponResponse response = couponService.validateCoupon("SAVE10");

        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals("SAVE10", response.getCode());
        assertEquals(BigDecimal.valueOf(10.00), response.getDiscountPercent());
        assertEquals("Coupon is valid", response.getMessage());
    }

    @Test
    void validateCoupon_notFound() {
        when(couponRepository.findByCodeIgnoreCase("INVALID")).thenReturn(Optional.empty());

        CouponResponse response = couponService.validateCoupon("INVALID");

        assertNotNull(response);
        assertFalse(response.isValid());
        assertEquals("Coupon code not found", response.getMessage());
    }

    @Test
    void validateCoupon_inactive() {
        when(couponRepository.findByCodeIgnoreCase("SAVE20")).thenReturn(Optional.of(inactiveCoupon));

        CouponResponse response = couponService.validateCoupon("SAVE20");

        assertNotNull(response);
        assertFalse(response.isValid());
        assertEquals("Coupon is inactive", response.getMessage());
    }

    @Test
    void validateCoupon_expired() {
        when(couponRepository.findByCodeIgnoreCase("SAVE30")).thenReturn(Optional.of(expiredCoupon));

        CouponResponse response = couponService.validateCoupon("SAVE30");

        assertNotNull(response);
        assertFalse(response.isValid());
        assertEquals("Coupon has expired", response.getMessage());
    }

    @Test
    void validateCoupon_emptyCode() {
        CouponResponse response = couponService.validateCoupon("   ");

        assertNotNull(response);
        assertFalse(response.isValid());
        assertEquals("Coupon code is empty", response.getMessage());
    }

    @Test
    void createCoupon_success() {
        when(couponRepository.findByCodeIgnoreCase("NEWCOUPON")).thenReturn(Optional.empty());
        when(couponRepository.save(any(CouponEntity.class))).thenAnswer(i -> i.getArgument(0));

        CouponEntity created = couponService.createCoupon("NEWCOUPON", BigDecimal.valueOf(15.00), LocalDateTime.now().plusDays(10));

        assertNotNull(created);
        assertEquals("NEWCOUPON", created.getCode());
        assertEquals(BigDecimal.valueOf(15.00), created.getDiscountPercent());
        assertTrue(created.isActive());
    }
}
