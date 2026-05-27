package com.ecom.ecomapp.cart;

import com.ecom.ecomapp.config.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(cartService.getCart(principal.id()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addItem(@AuthenticationPrincipal UserPrincipal principal,
                                                    @Valid @RequestBody CartItemRequest request) {
        CartItemResponse response = cartService.addItem(principal.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartItemResponse> updateItem(@AuthenticationPrincipal UserPrincipal principal,
                                                       @PathVariable Long itemId,
                                                       @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateItemQuantity(principal.id(), itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@AuthenticationPrincipal UserPrincipal principal,
                                           @PathVariable Long itemId) {
        cartService.removeItem(principal.id(), itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserPrincipal principal) {
        cartService.clearCart(principal.id());
        return ResponseEntity.noContent().build();
    }
}
