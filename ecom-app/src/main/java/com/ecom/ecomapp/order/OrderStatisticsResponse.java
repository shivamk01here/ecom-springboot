package com.ecom.ecomapp.order;

public class OrderStatisticsResponse {
    private long totalOrders;
    private long completedOrders;
    private long pendingOrders;
    private long cancelledOrders;

    public OrderStatisticsResponse(long totalOrders, long completedOrders, long pendingOrders, long cancelledOrders) {
        this.totalOrders = totalOrders;
        this.completedOrders = completedOrders;
        this.pendingOrders = pendingOrders;
        this.cancelledOrders = cancelledOrders;
    }

    public long getTotalOrders() { return totalOrders; }
    public long getCompletedOrders() { return completedOrders; }
    public long getPendingOrders() { return pendingOrders; }
    public long getCancelledOrders() { return cancelledOrders; }
}
