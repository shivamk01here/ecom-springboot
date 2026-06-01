package com.ecom.ecomapp.coupon;

import java.math.BigDecimal;

public class CouponResponse {

    private String code;
    private BigDecimal discountPercent;
    private boolean valid;
    private String message;

    public CouponResponse() {}

    public CouponResponse(String code, BigDecimal discountPercent, boolean valid, String message) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.valid = valid;
        this.message = message;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
