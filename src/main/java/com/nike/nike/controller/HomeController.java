package com.nike.nike.controller;

import org.springframework.security.core.Authentication;
import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.nike.nike.model.Product;
import com.nike.nike.service.ProductService;

@Controller
public class HomeController {

    private final ProductService productService;
    
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/","/home"})
    public String home(Model model , Authentication authentication) {
    String username = (authentication != null) ? authentication.getName() : "Guest";
    model.addAttribute("username", username);
    List<Product> products = productService.getAllProducts();
    model.addAttribute("products", products);
        return "home";
    }

}
