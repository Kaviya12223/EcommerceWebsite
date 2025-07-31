package com.ecommerce.E_commerce.controller;

import com.ecommerce.E_commerce.entity.Order;
import com.ecommerce.E_commerce.entity.User;
import com.ecommerce.E_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @Controller
    @RequestMapping("/payment")
    public static class PaymentController {

        @Autowired
        private UserService.OrderService orderService;

        @GetMapping("/{orderId}")
        public String showPaymentPage(@PathVariable Long orderId, Model model) {
            Order order = orderService.getOrderById(orderId);
            model.addAttribute("order", order);
            return "payment"; // This will map to payment.html
        }

        // This simulates processing the payment. In a real app, this would be a callback from a gateway.
        @PostMapping("/process/{orderId}")
        public String processPayment(@PathVariable Long orderId) {
            // Simulate payment processing logic (e.g., call external gateway API)
            // For now, we'll just immediately mark it as complete
            orderService.markOrderAsCompleted(orderId);
            return "redirect:/order/success/" + orderId; // Redirect to order success page
        }
    }
}
