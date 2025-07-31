package com.ecommerce.E_commerce.repository;

import com.ecommerce.E_commerce.entity.Category; // Import Category
import com.ecommerce.E_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List; // Import List

public interface ProductRepository extends JpaRepository<Product, Long> {
    // New: Find products by category
    List<Product> findByCategory(Category category);
}