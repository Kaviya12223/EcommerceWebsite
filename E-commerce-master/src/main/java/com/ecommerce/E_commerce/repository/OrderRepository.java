package com.ecommerce.E_commerce.repository;

import com.ecommerce.E_commerce.entity.Order;
import com.ecommerce.E_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user); // New: Find orders by User
}