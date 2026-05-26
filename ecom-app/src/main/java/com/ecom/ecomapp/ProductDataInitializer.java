package com.ecom.ecomapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductDataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductDataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.save(new ProductEntity("Laptop", "electronics", new BigDecimal("1499.99")));
            productRepository.save(new ProductEntity("Running Shoes", "sports", new BigDecimal("89.99")));
        }
    }
}
