package com.ecommerce.E_commerce.service;

import com.ecommerce.E_commerce.entity.*;
import com.ecommerce.E_commerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator; // Import Iterator
import java.util.List;

@Service
public class WishlistService {

    @Autowired private WishlistRepository wishlistRepository;
    @Autowired private WishlistProductRepository wishlistProductRepository; // Still inject for potential direct use, but won't use for removal now
    @Autowired private ProductRepository productRepository;

    public Wishlist getOrCreateWishlist(User user) {
        return wishlistRepository.findByUser(user).orElseGet(() -> {
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user);
            return wishlistRepository.save(wishlist);
        });
    }

    public void addToWishlist(User user, Long productId) {
        if (user == null) return;
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return;

        Wishlist wishlist = getOrCreateWishlist(user);

        // Ensure the collection is not null, especially if newly created or lazily loaded
        if (wishlist.getItems() == null) {
            wishlist.setItems(new java.util.ArrayList<>());
        }

        boolean exists = wishlist.getItems().stream()
                .anyMatch(wp -> wp.getProduct().getId().equals(productId));

        if (!exists) {
            WishlistProduct wp = new WishlistProduct();
            wp.setWishlist(wishlist); // Set the parent wishlist
            wp.setProduct(product);
            wishlist.getItems().add(wp); // Add to the collection
            wishlistRepository.save(wishlist); // Save the parent to persist the child
        }
    }

    public List<WishlistProduct> getWishlistItems(User user) {
        Wishlist wishlist = wishlistRepository.findByUser(user).orElse(null);
        // Ensure the items collection is not null before returning
        return (wishlist != null && wishlist.getItems() != null) ? wishlist.getItems() : List.of();
    }

    public void removeFromWishlist(User user, Long productId) {
        if (user == null) return;
        Wishlist wishlist = wishlistRepository.findByUser(user).orElse(null);
        if (wishlist == null) return;

        // Use an iterator to safely remove elements while iterating
        // This is crucial when modifying a collection that you are iterating over.
        Iterator<WishlistProduct> iterator = wishlist.getItems().iterator();
        while (iterator.hasNext()) {
            WishlistProduct wp = iterator.next();
            if (wp.getProduct().getId().equals(productId)) {
                iterator.remove(); // Remove from the collection
                // No need to call wishlistProductRepository.delete(wp) directly here
                // because orphanRemoval=true on the @OneToMany mapping will handle it
                // when the parent (wishlist) is saved.
                break; // Assuming one product per wishlist entry
            }
        }
        wishlistRepository.save(wishlist); // Save the parent to trigger the orphan removal
    }
}