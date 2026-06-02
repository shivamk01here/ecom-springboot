package com.ecom.ecomapp.order;

import com.ecom.ecomapp.analytics.CategorySalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(Long userId);
    
    Page<OrderEntity> findByUserIdOrderByOrderDateDesc(Long userId, Pageable pageable);
    
    Page<OrderEntity> findByUserIdAndStatusOrderByOrderDateDesc(Long userId, OrderStatus status, Pageable pageable);
    
    @Query("SELECT o FROM OrderEntity o WHERE o.user.id = :userId ORDER BY o.orderDate DESC")
    List<OrderEntity> findByUserIdOrderByOrderDateDesc(@Param("userId") Long userId);

    @Query("SELECT o FROM OrderEntity o WHERE o.user.id = :userId AND o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<OrderEntity> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM OrderEntity o WHERE o.user.id = :userId AND o.status = :status")
    List<OrderEntity> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") OrderStatus status);

    long countByUserId(Long userId);

    Optional<OrderEntity> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT new com.ecom.ecomapp.analytics.CategorySalesResponse(p.category, SUM(oi.quantity), SUM(oi.subtotal)) " +
           "FROM OrderItemEntity oi JOIN oi.product p GROUP BY p.category")
    List<CategorySalesResponse> findCategorySales();

    @Query("SELECT o FROM OrderEntity o ORDER BY o.orderDate DESC")
    List<OrderEntity> findRecentOrders(Pageable pageable);
}
