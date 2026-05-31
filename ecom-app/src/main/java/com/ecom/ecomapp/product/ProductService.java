package com.ecom.ecomapp.product;

import com.ecom.ecomapp.review.ReviewEntity;
import com.ecom.ecomapp.review.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    private ProductResponse enrichWithReviewStats(ProductEntity product) {
        List<ReviewEntity> reviews = reviewRepository.findByProductId(product.getId());
        double avg = reviews.stream()
                .mapToInt(ReviewEntity::getRating)
                .average()
                .orElse(0.0);
        avg = Math.round(avg * 10.0) / 10.0;
        int count = reviews.size();
        return new ProductResponse(product, avg, count);
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::enrichWithReviewStats)
                .toList();
    }

    public ProductResponse getById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return enrichWithReviewStats(product);
    }

    public ProductResponse create(ProductRequest request) {
        ProductEntity product = new ProductEntity(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getImageUrl(),
                request.getCategory(),
                request.getStock()
        );
        ProductEntity saved = productRepository.save(product);
        return enrichWithReviewStats(saved);
    }

    public void delete(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    public ProductResponse update(Long id, ProductRequest request) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(request.getCategory());
        product.setStock(request.getStock());
        ProductEntity updated = productRepository.save(product);
        return enrichWithReviewStats(updated);
    }

    public List<ProductResponse> search(String query) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream()
                .map(this::enrichWithReviewStats)
                .toList();
    }
}
