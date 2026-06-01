package com.ecom.ecomapp.coupon;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional(readOnly = true)
    public CouponResponse validateCoupon(String code) {
        if (code == null || code.trim().isEmpty()) {
            return new CouponResponse("", BigDecimal.ZERO, false, "Coupon code is empty");
        }

        var optCoupon = couponRepository.findByCodeIgnoreCase(code.trim());
        if (optCoupon.isEmpty()) {
            return new CouponResponse(code, BigDecimal.ZERO, false, "Coupon code not found");
        }

        CouponEntity coupon = optCoupon.get();

        if (!coupon.isActive()) {
            return new CouponResponse(coupon.getCode(), coupon.getDiscountPercent(), false, "Coupon is inactive");
        }

        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new CouponResponse(coupon.getCode(), coupon.getDiscountPercent(), false, "Coupon has expired");
        }

        return new CouponResponse(coupon.getCode(), coupon.getDiscountPercent(), true, "Coupon is valid");
    }

    @Transactional
    public CouponEntity createCoupon(String code, BigDecimal discountPercent, LocalDateTime expiryDate) {
        couponRepository.findByCodeIgnoreCase(code).ifPresent(c -> {
            throw new RuntimeException("Coupon already exists: " + code);
        });

        CouponEntity coupon = new CouponEntity(code.toUpperCase().trim(), discountPercent, expiryDate, true);
        return couponRepository.save(coupon);
    }
}
