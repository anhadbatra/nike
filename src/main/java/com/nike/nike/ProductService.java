package com.nike.nike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nike.nike.Models.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    // Save or update a product
    public void saveProduct(Product product) {
        product.setProductdate(LocalDate.now());
        productRepository.save(product);
    }
}
