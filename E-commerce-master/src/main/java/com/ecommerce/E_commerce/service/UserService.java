package com.ecommerce.E_commerce.service;

import com.ecommerce.E_commerce.entity.*;
import com.ecommerce.E_commerce.repository.CartRepository;
import com.ecommerce.E_commerce.repository.OrderRepository;
import com.ecommerce.E_commerce.repository.ProductRepository;
import com.ecommerce.E_commerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String email = authentication.getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Service
    public static class OrderService { // Renamed from OrderService to match the file name

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
                throw new IllegalStateException("Your cart is empty. Please add items before proceeding to checkout."); // More user-friendly message
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
                    throw new IllegalArgumentException("Not enough stock for product: '" + product.getName() + "'. Available: " + product.getQuantity() + ", Requested: " + quantity); // More detailed message
                }
                if (product.getQuantity() == 0) { // Explicit check for 0 stock
                    throw new IllegalArgumentException("Product '" + product.getName() + "' is currently out of stock.");
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

        public List<Order> getOrdersByUser(User user) {
            return orderRepository.findByUser(user);
        }
    }
}