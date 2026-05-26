package com.ecom.ecomapp;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        ProductEntity entity = new ProductEntity(request.name(), request.category(), request.price());
        return convert(productRepository.save(entity));
    }

    public List<Product> findAll(String category) {
        return (category == null || category.isBlank() ?
                productRepository.findAll() :
                productRepository.findByCategoryIgnoreCase(category)).stream()
                .map(this::convert)
                .toList();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .map(this::convert)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest request) {
        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        existing.setName(request.name());
        existing.setCategory(request.category());
        existing.setPrice(request.price());
        return convert(productRepository.save(existing));
    }

    @Transactional
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ProductNotFoundException(id);
        }
    }

    private Product convert(ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getCategory(), entity.getPrice());
    }

    public record Product(Long id, String name, String category, BigDecimal price) {
    }

    public record ProductRequest(String name, String category, BigDecimal price) {
    }
}
