package com.ecom.ecomapp.coupon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/validate")
    public ResponseEntity<CouponResponse> validateCoupon(@RequestParam String code) {
        return ResponseEntity.ok(couponService.validateCoupon(code));
    }
}
