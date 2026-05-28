package com.ecom.ecomapp.wishlist;

import com.ecom.ecomapp.config.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<List<WishlistItemResponse>> getWishlist(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(wishlistService.getWishlist(principal.id()));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getWishlistCount(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(wishlistService.getWishlistCount(principal.id()));
    }

    @GetMapping("/contains/{productId}")
    public ResponseEntity<Boolean> isInWishlist(@AuthenticationPrincipal UserPrincipal principal,
                                                 @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.isInWishlist(principal.id(), productId));
    }

    @PostMapping("/items")
    public ResponseEntity<WishlistItemResponse> addItem(@AuthenticationPrincipal UserPrincipal principal,
                                                        @Valid @RequestBody WishlistItemRequest request) {
        WishlistItemResponse response = wishlistService.addItem(principal.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@AuthenticationPrincipal UserPrincipal principal,
                                           @PathVariable Long itemId) {
        wishlistService.removeItem(principal.id(), itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> removeByProductId(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long productId) {
        wishlistService.removeByProductId(principal.id(), productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearWishlist(@AuthenticationPrincipal UserPrincipal principal) {
        wishlistService.clearWishlist(principal.id());
        return ResponseEntity.noContent().build();
    }
}
