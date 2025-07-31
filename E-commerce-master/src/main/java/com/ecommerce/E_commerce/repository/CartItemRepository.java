package com.ecommerce.E_commerce.repository;

import com.ecommerce.E_commerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
