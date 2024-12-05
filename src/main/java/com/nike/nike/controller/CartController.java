package com.nike.nike.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nike.nike.model.Cart;
import com.nike.nike.model.User;
import com.nike.nike.service.CartService;
import com.nike.nike.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
       this.cartService = cartService;
       this.userService = userService;
    }

    // Display cart items
    @GetMapping
    public String showCart(Model model, Authentication authentication) {
       User user = userService.findByUsername(authentication.getName());
       Cart cart = cartService.getCartWithItems(user);
       model.addAttribute("cart", cart);
       return "cart";
    }

    // Add product to cart
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            Authentication authentication) {
       User user = userService.findByUsername(authentication.getName());
       cartService.addProductToCart(user, productId, quantity);
       return "redirect:/cart";
    }

    // Update product quantity in cart
    @PostMapping("/update")
    public String updateCart(@RequestParam Long productId,
                             @RequestParam int quantity,
                             Authentication authentication) {
       User user = userService.findByUsername(authentication.getName());
       cartService.updateProductQuantity(user, productId, quantity);
       return "redirect:/cart";
    }

    // Remove product from cart
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId,
                                 Authentication authentication) {
       User user = userService.findByUsername(authentication.getName());
       cartService.removeProductFromCart(user, productId);
       return "redirect:/cart";
    }

    // Clear the cart
    @PostMapping("/clear") 
    public String clearCart(Authentication authentication) {
       User user = userService.findByUsername(authentication.getName());
       cartService.clearCart(user);
       return "redirect:/cart";
    }
}
