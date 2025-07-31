package com.ecommerce.E_commerce.repository;

import com.ecommerce.E_commerce.entity.WishlistProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistProductRepository extends JpaRepository<WishlistProduct, Long> {
}
