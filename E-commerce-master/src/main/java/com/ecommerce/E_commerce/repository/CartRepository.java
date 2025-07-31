package com.ecommerce.E_commerce.repository;

import com.ecommerce.E_commerce.entity.Cart;
import com.ecommerce.E_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
