package com.ecommerce.E_commerce.controller;

import com.ecommerce.E_commerce.entity.User;
import com.ecommerce.E_commerce.entity.Order;
import com.ecommerce.E_commerce.service.CartService;
import com.ecommerce.E_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Import RedirectAttributes

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private UserService userService;
    @Autowired private UserService.OrderService orderService;

    @GetMapping("/view")
    public String viewCart(Model model, @RequestParam(name = "error", required = false) String errorMessage) {
        User user = userService.getCurrentUser();
        model.addAttribute("items", cartService.getCartItems(user));
        model.addAttribute("cartTotal", cartService.getCartTotal(user));
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage); // Pass error message to the model
        }
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId) {
        User user = userService.getCurrentUser();
        cartService.addToCart(user, productId);
        return "redirect:/cart/view";
    }

    @PostMapping("/increase/{productId}")
    public String increaseQuantity(@PathVariable Long productId) {
        User user = userService.getCurrentUser();
        cartService.addToCart(user, productId);
        return "redirect:/cart/view";
    }

    @PostMapping("/decrease/{productId}")
    public String decreaseQuantity(@PathVariable Long productId) {
        User user = userService.getCurrentUser();
        cartService.decreaseQuantity(user, productId);
        return "redirect:/cart/view";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        User user = userService.getCurrentUser();
        cartService.removeFromCart(user, productId);
        return "redirect:/cart/view";
    }

    @PostMapping("/proceedToBuy")
    public String proceedToBuy(RedirectAttributes redirectAttributes) { // Use RedirectAttributes
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        try {
            Order order = orderService.createOrderFromCart(user);
            return "redirect:/payment/" + order.getId();
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // Pass message for empty cart
            System.err.println("Error creating order (IllegalState): " + e.getMessage());
            return "redirect:/cart/view";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // Pass message for stock issues
            System.err.println("Error creating order (IllegalArgument): " + e.getMessage());
            return "redirect:/cart/view";
        }
    }
}