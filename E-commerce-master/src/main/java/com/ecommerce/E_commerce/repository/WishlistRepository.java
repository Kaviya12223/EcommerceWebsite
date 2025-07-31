package com.ecommerce.E_commerce.repository;

import com.ecommerce.E_commerce.entity.User;
import com.ecommerce.E_commerce.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUser(User user);
}
