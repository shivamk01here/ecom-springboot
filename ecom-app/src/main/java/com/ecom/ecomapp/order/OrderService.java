package com.ecom.ecomapp.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.ecomapp.cart.CartItemEntity;
import com.ecom.ecomapp.cart.CartItemRepository;
import com.ecom.ecomapp.product.ProductEntity;
import com.ecom.ecomapp.product.ProductRepository;
import com.ecom.ecomapp.user.UserEntity;
import com.ecom.ecomapp.user.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartItemRepository cartItemRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrderHistory(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId).stream()
                .map(OrderResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrderHistoryPaginated(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId, pageable)
                .map(OrderResponse::new);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrdersByStatus(Long userId, OrderStatus status, Pageable pageable) {
        return orderRepository.findByUserIdAndStatusOrderByOrderDateDesc(userId, status, pageable)
                .map(OrderResponse::new);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId, Long userId) {
        OrderEntity order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return new OrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByUserIdAndDateRange(userId, startDate, endDate).stream()
                .map(OrderResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(Long userId, OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status).stream()
                .map(OrderResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public long getTotalOrders(Long userId) {
        return orderRepository.countByUserId(userId);
    }

    @Transactional(readOnly = true)
    public OrderStatisticsResponse getOrderStatistics(Long userId) {
        List<OrderEntity> allOrders = orderRepository.findByUserId(userId);
        
        long totalOrders = allOrders.size();
        long completedOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .count();
        long pendingOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.PENDING || o.getStatus() == OrderStatus.CONFIRMED)
                .count();
        long cancelledOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CANCELLED)
                .count();

        return new OrderStatisticsResponse(totalOrders, completedOrders, pendingOrders, cancelledOrders);
    }

    @Transactional
    public OrderResponse checkout(Long userId, CheckoutRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        List<CartItemEntity> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        OrderEntity order = new OrderEntity(user, totalAmount);
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setNotes(request.getNotes());

        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (CartItemEntity cartItem : cartItems) {
            ProductEntity product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItemEntity orderItem = new OrderItemEntity(
                    order,
                    product,
                    cartItem.getQuantity(),
                    product.getPrice()
            );
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        OrderEntity savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByUserId(userId);

        return new OrderResponse(savedOrder);
    }
}
