package com.ecommerce.E_commerce.controller;

import com.ecommerce.E_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; // Import RequestParam

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping({"/", "/home"})
    public String home(Model model,
                       @RequestParam(name = "categoryId", required = false) Long categoryId) { // Add categoryId parameter
        if (categoryId != null) {
            model.addAttribute("products", productService.getProductsByCategoryId(categoryId)); // Get products by category
        } else {
            model.addAttribute("products", productService.getAllProducts()); // Get all products
        }
        model.addAttribute("categories", productService.getAllCategories()); // Add categories to the model for sidebar
        return "home";
    }
}