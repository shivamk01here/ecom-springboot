package com.ecom.ecomapp.cart;

import com.ecom.ecomapp.product.ProductEntity;
import com.ecom.ecomapp.product.ProductNotFoundException;
import com.ecom.ecomapp.product.ProductRepository;
import com.ecom.ecomapp.user.UserEntity;
import com.ecom.ecomapp.user.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<CartItemResponse> getCart(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(CartItemResponse::new)
                .toList();
    }

    @Transactional
    public CartItemResponse addItem(Long userId, CartItemRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        var existing = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existing.isPresent()) {
            CartItemEntity item = existing.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            return new CartItemResponse(cartItemRepository.save(item));
        }

        CartItemEntity item = new CartItemEntity(user, product, request.getQuantity());
        return new CartItemResponse(cartItemRepository.save(item));
    }

    @Transactional
    public CartItemResponse updateItemQuantity(Long userId, Long itemId, int quantity) {
        CartItemEntity item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getUser().getId().equals(userId)) {
            throw new BadCredentialsException("Not your cart item");
        }
        item.setQuantity(quantity);
        return new CartItemResponse(cartItemRepository.save(item));
    }

    @Transactional
    public void removeItem(Long userId, Long itemId) {
        CartItemEntity item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getUser().getId().equals(userId)) {
            throw new BadCredentialsException("Not your cart item");
        }
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
