package com.ecom.ecomapp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Manage products with HATEOAS-enabled endpoints")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "List products with optional category filtering")
    public ResponseEntity<CollectionModel<EntityModel<ProductResponse>>> listProducts(
            @RequestParam Optional<String> category) {
        List<EntityModel<ProductResponse>> content = productService.findAll(category.orElse(null)).stream()
                .map(this::toProductModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ProductResponse>> collection = CollectionModel.of(content,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                        .listProducts(category)).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single product by id")
    public ResponseEntity<EntityModel<ProductResponse>> getProduct(@PathVariable Long id) {
        ProductService.Product product = productService.findById(id);
        return ResponseEntity.ok(toProductModel(product));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<EntityModel<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        ProductService.Product product = productService.createProduct(request.toServiceRequest());
        EntityModel<ProductResponse> model = toProductModel(product);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product")
    public ResponseEntity<EntityModel<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductService.Product product = productService.updateProduct(id, request.toServiceRequest());
        return ResponseEntity.ok(toProductModel(product));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<ProductResponse> toProductModel(ProductService.Product product) {
        ProductResponse response = new ProductResponse(product.id(), product.name(), product.category(), product.price());
        return EntityModel.of(response,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                        .getProduct(product.id())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                        .listProducts(Optional.empty())).withRel("products"));
    }

    public static record ProductRequest(
            @NotBlank String name,
            @NotBlank String category,
            @Positive BigDecimal price) {

        public ProductService.ProductRequest toServiceRequest() {
            return new ProductService.ProductRequest(name, category, price);
        }
    }

    public static record ProductResponse(Long id, String name, String category, BigDecimal price) {
    }
}
