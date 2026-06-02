package com.ecom.ecomapp.analytics;

import com.ecom.ecomapp.order.OrderResponse;
import java.math.BigDecimal;
import java.util.List;

public class DashboardStatsResponse {

    private BigDecimal totalRevenue;
    private long totalOrdersCount;
    private long totalUsersCount;
    private List<CategorySalesResponse> categorySales;
    private List<OrderResponse> recentOrders;

    public DashboardStatsResponse() {}

    public DashboardStatsResponse(BigDecimal totalRevenue, long totalOrdersCount, long totalUsersCount,
                                  List<CategorySalesResponse> categorySales, List<OrderResponse> recentOrders) {
        this.totalRevenue = totalRevenue;
        this.totalOrdersCount = totalOrdersCount;
        this.totalUsersCount = totalUsersCount;
        this.categorySales = categorySales;
        this.recentOrders = recentOrders;
    }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public long getTotalOrdersCount() { return totalOrdersCount; }
    public void setTotalOrdersCount(long totalOrdersCount) { this.totalOrdersCount = totalOrdersCount; }

    public long getTotalUsersCount() { return totalUsersCount; }
    public void setTotalUsersCount(long totalUsersCount) { this.totalUsersCount = totalUsersCount; }

    public List<CategorySalesResponse> getCategorySales() { return categorySales; }
    public void setCategorySales(List<CategorySalesResponse> categorySales) { this.categorySales = categorySales; }

    public List<OrderResponse> getRecentOrders() { return recentOrders; }
    public void setRecentOrders(List<OrderResponse> recentOrders) { this.recentOrders = recentOrders; }
}
