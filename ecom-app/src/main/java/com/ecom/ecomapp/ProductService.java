package com.ecom.ecomapp;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public ProductService() {
        createProduct(new ProductRequest("Laptop", "electronics", new BigDecimal("1499.99")));
        createProduct(new ProductRequest("Running Shoes", "sports", new BigDecimal("89.99")));
    }

    public Product createProduct(ProductRequest request) {
        Long id = nextId.getAndIncrement();
        Product product = new Product(id, request.name(), request.category(), request.price());
        products.put(id, product);
        return product;
    }

    public List<Product> findAll(String category) {
        if (category == null || category.isBlank()) {
            return new ArrayList<>(products.values());
        }
        List<Product> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.category().equalsIgnoreCase(category)) {
                result.add(product);
            }
        }
        return result;
    }

    public Product findById(Long id) {
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    public Product updateProduct(Long id, ProductRequest request) {
        Product existing = findById(id);
        Product updated = new Product(existing.id(), request.name(), request.category(), request.price());
        products.put(id, updated);
        return updated;
    }

    public void deleteProduct(Long id) {
        if (products.remove(id) == null) {
            throw new ProductNotFoundException(id);
        }
    }

    public record Product(Long id, String name, String category, BigDecimal price) {
    }

    public record ProductRequest(String name, String category, BigDecimal price) {
    }
}
