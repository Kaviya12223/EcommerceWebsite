package com.ecommerce.E_commerce.service;

import com.ecommerce.E_commerce.entity.Cart;
import com.ecommerce.E_commerce.entity.CartItem;
import com.ecommerce.E_commerce.entity.Product;
import com.ecommerce.E_commerce.entity.User;
import com.ecommerce.E_commerce.repository.CartRepository;
import com.ecommerce.E_commerce.repository.CartItemRepository;
import com.ecommerce.E_commerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional; // Ensure this is imported

@Service
public class CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    public void addToCart(User user, Long productId) {
        if (user == null) return;
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return;

        Cart cart = getOrCreateCart(user);

        // Ensure the collection is not null, especially if newly created or lazily loaded
        if (cart.getItems() == null) {
            cart.setItems(new java.util.ArrayList<>());
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
        }
    }

    public List<CartItem> getCartItems(User user) {
        Cart cart = cartRepository.findByUser(user).orElse(null);
        // Ensure the items collection is not null before returning
        return (cart != null && cart.getItems() != null) ? cart.getItems() : List.of();
    }

    public void decreaseQuantity(User user, Long productId) {
        if (user == null) return;
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) return;

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst().orElse(null);

        if (item != null) {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                cartItemRepository.save(item);
            } else {
                // If quantity becomes 0, remove the item
                cart.getItems().remove(item); // Remove from the collection in the Cart entity
                cartItemRepository.delete(item); // Explicitly delete the CartItem from the database
            }
        }
    }

    public void removeFromCart(User user, Long productId) {
        if (user == null) return;
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) return;

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (itemToRemove != null) {
            cart.getItems().remove(itemToRemove); // Remove from the collection in the Cart entity
            cartItemRepository.delete(itemToRemove); // Explicitly delete the CartItem from the database
        }
    }

    public BigDecimal getCartTotal(User user) {
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null || cart.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct() != null && item.getProduct().getPrice() != null) {
                BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }
}