package com.ecommerce.E_commerce.service;

import com.ecommerce.E_commerce.entity.*;
import com.ecommerce.E_commerce.repository.CartRepository;
import com.ecommerce.E_commerce.repository.OrderRepository;
import com.ecommerce.E_commerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrderFromCart(User user) {
        Cart cart = cartRepository.findByUser(user).orElse(null);

        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order from an empty cart.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING_PAYMENT");

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            // Check stock availability
            if (product.getQuantity() < quantity) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPriceAtOrder(product.getPrice());
            order.addOrderItem(orderItem);

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            // Reduce product stock
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        // Clear the user's cart after creating the order
        cartRepository.delete(cart);

        return order;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }

    @Transactional
    public void markOrderAsCompleted(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus("COMPLETED");
        orderRepository.save(order);
        System.out.println("Order " + orderId + " marked as COMPLETED.");
    }

    // New method to get orders by user
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}