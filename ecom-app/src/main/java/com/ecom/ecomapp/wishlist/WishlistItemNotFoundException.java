package com.ecom.ecomapp.wishlist;

public class WishlistItemNotFoundException extends RuntimeException {
    public WishlistItemNotFoundException(Long itemId) {
        super("Wishlist item with ID " + itemId + " not found");
    }

    public WishlistItemNotFoundException(String message) {
        super(message);
    }
}
