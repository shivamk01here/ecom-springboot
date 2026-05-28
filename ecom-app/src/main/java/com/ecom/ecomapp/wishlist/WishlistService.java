package com.ecom.ecomapp.wishlist;

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
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistService(WishlistItemRepository wishlistItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<WishlistItemResponse> getWishlist(Long userId) {
        return wishlistItemRepository.findByUserId(userId).stream()
                .map(WishlistItemResponse::new)
                .toList();
    }

    @Transactional
    public WishlistItemResponse addItem(Long userId, WishlistItemRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        var existing = wishlistItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existing.isPresent()) {
            return new WishlistItemResponse(existing.get());
        }

        WishlistItemEntity item = new WishlistItemEntity(user, product);
        return new WishlistItemResponse(wishlistItemRepository.save(item));
    }

    @Transactional
    public void removeItem(Long userId, Long itemId) {
        WishlistItemEntity item = wishlistItemRepository.findById(itemId)
                .orElseThrow(() -> new WishlistItemNotFoundException(itemId));
        if (!item.getUser().getId().equals(userId)) {
            throw new BadCredentialsException("Not your wishlist item");
        }
        wishlistItemRepository.delete(item);
    }

    @Transactional
    public void removeByProductId(Long userId, Long productId) {
        var item = wishlistItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new WishlistItemNotFoundException("Product not in wishlist"));
        wishlistItemRepository.delete(item);
    }

    @Transactional
    public void clearWishlist(Long userId) {
        wishlistItemRepository.deleteByUserId(userId);
    }

    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistItemRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }

    public long getWishlistCount(Long userId) {
        return wishlistItemRepository.findByUserId(userId).size();
    }
}
