package com.ecom.ecomapp.order;

import jakarta.validation.constraints.NotBlank;

public class CheckoutRequest {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Billing address is required")
    private String billingAddress;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String notes;
    private String couponCode;

    public CheckoutRequest() {}

    public CheckoutRequest(String shippingAddress, String billingAddress, String phoneNumber, String notes) {
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }

    public CheckoutRequest(String shippingAddress, String billingAddress, String phoneNumber, String notes, String couponCode) {
        this(shippingAddress, billingAddress, phoneNumber, notes);
        this.couponCode = couponCode;
    }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
}
