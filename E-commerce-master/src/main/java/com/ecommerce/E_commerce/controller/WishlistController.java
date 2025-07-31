package com.ecommerce.E_commerce.controller;

import com.ecommerce.E_commerce.entity.User;
import com.ecommerce.E_commerce.service.UserService;
import com.ecommerce.E_commerce.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired private WishlistService wishlistService;
    @Autowired private UserService userService;

    @GetMapping("/view")
    public String viewWishlist(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("items", wishlistService.getWishlistItems(user));
        return "wishlist";
    }

    // Change to @PostMapping
    @PostMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId) {
        User user = userService.getCurrentUser();
        wishlistService.addToWishlist(user, productId);
        return "redirect:/wishlist/view";
    }

    // Change to @PostMapping
    @PostMapping("/remove/{productId}")
    public String removeFromWishlist(@PathVariable Long productId) {
        User user = userService.getCurrentUser();
        wishlistService.removeFromWishlist(user, productId);
        return "redirect:/wishlist/view";
    }
}