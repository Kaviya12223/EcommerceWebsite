package com.ecommerce.E_commerce.controller;

import com.ecommerce.E_commerce.entity.Category;
import com.ecommerce.E_commerce.entity.Product;
import com.ecommerce.E_commerce.entity.User;
import com.ecommerce.E_commerce.service.ProductService;
import com.ecommerce.E_commerce.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        return "add_product";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "images");
            String fileName = imageFile.getOriginalFilename();

            if (fileName != null && !fileName.isBlank()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.createDirectories(uploadPath);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                product.setImageName(fileName);
            }

            Category fullCategory = productService.getCategoryById(product.getCategory().getId());
            product.setCategory(fullCategory);

            User currentUser = userService.getCurrentUser();
            product.setSeller(currentUser);

            productService.saveProduct(product);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }

    @PostMapping("/delete/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public String deleteProduct(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        Product productToDelete = productService.getProductById(productId);

        if (productToDelete != null) {
            if (currentUser.getRole().equals("ROLE_ADMIN") ||
                    (currentUser.getRole().equals("ROLE_SELLER") && productToDelete.getSeller() != null && productToDelete.getSeller().getId().equals(currentUser.getId()))) {
                productService.deleteProduct(productId);
                redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
            } else {
                System.out.println("User " + currentUser.getEmail() + " attempted to delete product " + productId + " without authorization.");
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete this product.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
        }

        return "redirect:/home";
    }

    @PostMapping("/updateStock/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public String updateProductStock(@PathVariable Long productId,
                                     @RequestParam("quantity") int quantity,
                                     RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        Product productToUpdate = productService.getProductById(productId);

        if (productToUpdate != null) {
            if (currentUser.getRole().equals("ROLE_ADMIN") ||
                    (currentUser.getRole().equals("ROLE_SELLER") && productToUpdate.getSeller() != null && productToUpdate.getSeller().getId().equals(currentUser.getId()))) {
                if (quantity >= 0) {
                    productService.updateProductStock(productId, quantity);
                    redirectAttributes.addFlashAttribute("successMessage", "Stock updated successfully for " + productToUpdate.getName());
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Stock quantity cannot be negative.");
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to update stock for this product.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
        }
        return "redirect:/home";
    }
}