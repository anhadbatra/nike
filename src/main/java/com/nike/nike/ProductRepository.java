package com.nike.nike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nike.nike.Models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
