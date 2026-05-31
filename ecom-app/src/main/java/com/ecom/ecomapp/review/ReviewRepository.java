package com.ecom.ecomapp.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByProductId(Long productId);
    boolean existsByProductIdAndUserId(Long productId, Long userId);
    Optional<ReviewEntity> findByProductIdAndUserId(Long productId, Long userId);
}
