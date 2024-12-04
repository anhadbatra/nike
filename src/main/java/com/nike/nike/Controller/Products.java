package com.nike.nike.Controller;

import java.io.IOException;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nike.nike.ProductRepository;
import com.nike.nike.Models.Product;

@Controller
public class Products {
    @Autowired
    private ProductRepository productRepository; 

    @GetMapping("/products")
    public String listProducts(Model model) {
        List <Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products"; // Renders products.html
    }
    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable("id") Integer id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-details"; // Render the product details page
        } else {
            return "error"; // Handle case where product is not found
        }
    }
    @GetMapping("/products-mgmt")
    public String AllProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "productmgmt";
    }
    @GetMapping("product-mgmt/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return "productmgmt";
    }
    

    // Display the add product form
    @GetMapping("/add-product")
    public String addProductForm() {
        return "addproduct";
    }

    // Handle adding a new product
    @PostMapping("/add-product")
    public String addProduct(
        @RequestParam("productname") String productname,
        @RequestParam("price") int price,
        @RequestParam("description") String description,
        @RequestParam("profileImage") MultipartFile profileImage,
        @RequestParam("bannerImage") MultipartFile bannerImage,
        @RequestParam("thumbnailImage") MultipartFile thumbnailImage) throws IOException {
        String uploadDir = "D:\\Developing_Web_Applications\\n" + //
                        "ike\\src\\main\\resources\\uploaded-images\\"; 
        
        Product product = new Product();
        product.setProductName(productname);
        product.setPrice(price);
        product.setDescription(description);
        product.setProductdate(LocalDate.now());
        if (!profileImage.isEmpty()) {
            String profileImagePath = uploadDir + profileImage.getOriginalFilename();
            profileImage.transferTo(new java.io.File(profileImagePath));
            product.setProfileImagePath("/uploaded-images/" + profileImage.getOriginalFilename());
        }
    
        // Save banner image
        if (!bannerImage.isEmpty()) {
            String bannerImagePath = uploadDir + bannerImage.getOriginalFilename();
            bannerImage.transferTo(new java.io.File(bannerImagePath));
            product.setBannerImagePath("/uploaded-images/" + bannerImage.getOriginalFilename());
        }
    
        // Save thumbnail image
        if (!thumbnailImage.isEmpty()) {
            String thumbnailImagePath = uploadDir + thumbnailImage.getOriginalFilename();
            thumbnailImage.transferTo(new java.io.File(thumbnailImagePath));
            product.setThumbnailImagePath("/uploaded-images/" + thumbnailImage.getOriginalFilename());
        }
        productRepository.save(product);
        return "products";
        
    }
    
}
