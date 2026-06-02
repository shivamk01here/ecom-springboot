package com.ecom.ecomapp.analytics;

import com.ecom.ecomapp.order.OrderEntity;
import com.ecom.ecomapp.order.OrderRepository;
import com.ecom.ecomapp.user.Role;
import com.ecom.ecomapp.user.UserEntity;
import com.ecom.ecomapp.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    private UserEntity user;
    private OrderEntity order1;
    private OrderEntity order2;
    private CategorySalesResponse categorySales;

    @BeforeEach
    void setUp() {
        user = new UserEntity("admin@example.com", "password", "Admin User", Role.ADMIN);

        order1 = new OrderEntity(user, BigDecimal.valueOf(100.00));
        order2 = new OrderEntity(user, BigDecimal.valueOf(150.00));

        categorySales = new CategorySalesResponse("Electronics", 5, BigDecimal.valueOf(500.00));
    }

    @Test
    void getDashboardStatistics_success() {
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));
        when(orderRepository.count()).thenReturn(2L);
        when(userRepository.count()).thenReturn(1L);
        when(orderRepository.findCategorySales()).thenReturn(List.of(categorySales));
        when(orderRepository.findRecentOrders(any(Pageable.class))).thenReturn(List.of(order1, order2));

        DashboardStatsResponse stats = analyticsService.getDashboardStatistics();

        assertNotNull(stats);
        assertEquals(BigDecimal.valueOf(250.00), stats.getTotalRevenue());
        assertEquals(2L, stats.getTotalOrdersCount());
        assertEquals(1L, stats.getTotalUsersCount());
        
        assertEquals(1, stats.getCategorySales().size());
        assertEquals("Electronics", stats.getCategorySales().get(0).getCategory());
        assertEquals(5, stats.getCategorySales().get(0).getTotalSalesCount());
        assertEquals(BigDecimal.valueOf(500.00), stats.getCategorySales().get(0).getTotalRevenue());

        assertEquals(2, stats.getRecentOrders().size());
        verify(orderRepository, times(1)).findAll();
        verify(orderRepository, times(1)).count();
        verify(userRepository, times(1)).count();
        verify(orderRepository, times(1)).findCategorySales();
        verify(orderRepository, times(1)).findRecentOrders(any(Pageable.class));
    }
}
