package com.ecom.ecomapp.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategory(String category);
    List<ProductEntity> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
