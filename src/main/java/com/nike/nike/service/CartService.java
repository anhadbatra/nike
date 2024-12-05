package com.nike.nike.service;

import java.util.List;

import com.nike.nike.model.Cart;
import com.nike.nike.model.CartItem;
import com.nike.nike.model.User;

public interface CartService {
    void addProductToCart(User user, Long productId, int quantity);
    void removeProductFromCart(User user, Long productId);
    void updateProductQuantity(User user, Long productId, int quantity);
    List<CartItem> getCartItems(User user);
    void clearCart(User user);
    Cart getCartWithItems(User user);
}
