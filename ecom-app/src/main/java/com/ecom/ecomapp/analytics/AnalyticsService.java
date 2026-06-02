package com.ecom.ecomapp.analytics;

import com.ecom.ecomapp.order.OrderEntity;
import com.ecom.ecomapp.order.OrderRepository;
import com.ecom.ecomapp.order.OrderResponse;
import com.ecom.ecomapp.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public AnalyticsService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStatistics() {
        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .map(OrderEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalOrdersCount = orderRepository.count();
        long totalUsersCount = userRepository.count();

        List<CategorySalesResponse> categorySales = orderRepository.findCategorySales();

        List<OrderResponse> recentOrders = orderRepository.findRecentOrders(PageRequest.of(0, 5)).stream()
                .map(OrderResponse::new)
                .toList();

        return new DashboardStatsResponse(
                totalRevenue,
                totalOrdersCount,
                totalUsersCount,
                categorySales,
                recentOrders
        );
    }
}
