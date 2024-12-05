package com.nike.nike.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nike.nike.model.Product;
import com.nike.nike.model.User;
import com.nike.nike.service.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
       this.productService = productService;
    }

    // Display all products
    @GetMapping
    public String listProducts(Model model) {
    List<Product> products = productService.getAllProducts();
    model.addAttribute("products", products);
    return "products";
}

    // Display product details
    @GetMapping("/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
       Product product = productService.getProductById(id);
       model.addAttribute("product", product);
       return "product_details";
    }

    // Admin: Show form to add a new product
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    // Admin: Save new product
   @PostMapping(value = "/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
                          @RequestParam("imageFile") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
    if (bindingResult.hasErrors()) {
        return "product_form";
    }

    if (!file.isEmpty()) {
        String uploadDir = "D:/Developing_Web_Applications/nike/src/main/resources/static/images";// Ensure this directory exists or create it
        Path uploadPath = Path.of(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        product.setImagePath("/images/" + file.getOriginalFilename());
    }

    productService.saveProduct(product);
    redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
    return "redirect:/products";
}


    // Admin: Show form to edit a product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
       Product product = productService.getProductById(id);
       model.addAttribute("product", product);
       return "product_form";
    }

    // Admin: Update product
    @PostMapping("/{id}")
    public String modifyProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
                          @RequestParam("imageFile") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
    if (bindingResult.hasErrors()) {
        return "product_form";
    }

    if (!file.isEmpty()) {
        String uploadDir = "D:/Developing_Web_Applications/nike/src/main/resources/static/images";// Ensure this directory exists or create it
        Path uploadPath = Path.of(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        product.setImagePath("/images/" + file.getOriginalFilename());
    }

    productService.saveProduct(product);
    redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
    return "redirect:/products";
}
    // Admin: Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
       productService.deleteProduct(id);
       return "redirect:/products";
    }
}
