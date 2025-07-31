package com.ecommerce.E_commerce.controller;

import com.ecommerce.E_commerce.entity.Order;
import com.ecommerce.E_commerce.entity.User; // Import User
import com.ecommerce.E_commerce.service.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List; // Import List

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserService.OrderService orderService;

    @Autowired // Inject UserService
    private UserService userService;

    @GetMapping("/success/{orderId}")
    public String showOrderSuccessPage(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order_success";
    }

    // Method to view previous orders for a user
    @GetMapping("/history")
    public String viewOrderHistory(Model model) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            // Redirect to login or show an error if no user is logged in
            return "redirect:/login";
        }
        List<Order> orders = orderService.getOrdersByUser(currentUser);
        model.addAttribute("orders", orders);
        return "order_history"; // This will map to order_history.html
    }
}