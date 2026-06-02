package com.ecom.ecomapp.analytics;

import java.math.BigDecimal;

public class CategorySalesResponse {

    private String category;
    private long totalSalesCount;
    private BigDecimal totalRevenue;

    public CategorySalesResponse() {}

    public CategorySalesResponse(String category, long totalSalesCount, BigDecimal totalRevenue) {
        this.category = category;
        this.totalSalesCount = totalSalesCount;
        this.totalRevenue = totalRevenue;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public long getTotalSalesCount() { return totalSalesCount; }
    public void setTotalSalesCount(long totalSalesCount) { this.totalSalesCount = totalSalesCount; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
}
