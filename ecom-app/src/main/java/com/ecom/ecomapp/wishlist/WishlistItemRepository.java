package com.ecom.ecomapp.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItemEntity, Long> {
    List<WishlistItemEntity> findByUserId(Long userId);
    Optional<WishlistItemEntity> findByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserId(Long userId);
    void deleteByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT COUNT(w) FROM WishlistItemEntity w WHERE w.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT w FROM WishlistItemEntity w WHERE w.user.id = :userId ORDER BY w.addedAt DESC")
    List<WishlistItemEntity> findByUserIdOrderByAddedAtDesc(@Param("userId") Long userId);
}
