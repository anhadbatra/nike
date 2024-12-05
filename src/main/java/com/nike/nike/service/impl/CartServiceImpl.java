package com.nike.nike.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nike.nike.model.Cart;
import com.nike.nike.model.CartItem;
import com.nike.nike.model.Product;
import com.nike.nike.model.User;
import com.nike.nike.repository.CartItemRepository;
import com.nike.nike.repository.CartRepository;
import com.nike.nike.repository.ProductRepository;
import com.nike.nike.service.CartService;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    private Cart getCartByUser(User user) {
        return cartRepository.findByUserWithItems(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    @Override
    public void addProductToCart(User user, Long productId, int quantity) {
        Cart cart = getCartByUser(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.addCartItem(cartItem); // Use helper method
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public void removeProductFromCart(User user, Long productId) {
        Cart cart = getCartByUser(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartItemRepository.findByCartAndProduct(cart, product)
                .ifPresent(cartItem -> {
                    cart.removeCartItem(cartItem); // Use helper method
                    cartItemRepository.delete(cartItem);
                });
    }

    @Override
    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        for (CartItem item : new ArrayList<>(cart.getCartItems())) {
            cart.removeCartItem(item); // Use helper method
            cartItemRepository.delete(item);
        }
    }

    @Override
    public void updateProductQuantity(User user, Long productId, int quantity) {
        Cart cart = getCartByUser(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartItemRepository.findByCartAndProduct(cart, product)
                .ifPresent(cartItem -> {
                    cartItem.setQuantity(quantity);
                    cartItemRepository.save(cartItem);
                });
    }

    @Override
    public List<CartItem> getCartItems(User user) {
        Cart cart = getCartByUser(user);
        return cart.getCartItems();
    }
    
    @Override
    public Cart getCartWithItems(User user) {
        return getCartByUser(user);
    }
}
