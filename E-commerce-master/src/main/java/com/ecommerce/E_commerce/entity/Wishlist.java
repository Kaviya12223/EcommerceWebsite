package com.ecommerce.E_commerce.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistProduct> products;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<WishlistProduct> getItems() {
        return products;
    }

    public void setItems(List<WishlistProduct> items) {
        this.products = items;
    }


    public List<WishlistProduct> getProducts() { return products; }
    public void setProducts(List<WishlistProduct> products) { this.products = products; }
}
