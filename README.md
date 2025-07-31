# EcommerceWebsite
# 🛒 E-commerce Website (Spring Boot)

An online E-commerce web application built using Spring Boot, Thymeleaf, and MySQL. This project provides functionality for managing products, categories, and user authentication with a secure interface.

---

## 🚀 Features
- User Registration & Login (Spring Security)
- Product Listing & Categorization
- Shopping Cart Functionality
- Admin Panel for Product Management
- Responsive UI with Thymeleaf Templates
- H2 (in-memory) & MySQL (runtime) DB support

---

## 🧰 Tools & Technologies Used
| Technology            | Purpose                                 |
|-----------------------|-----------------------------------------|
| **Java 17**           | Core programming language               |
| **Spring Boot 3.5.3** | Framework for building the application  |
| **Spring Security**   | User authentication & authorization     |
| **Thymeleaf**         | Template engine for rendering HTML      |
| **MySQL**             | Runtime database                        |
| **H2 Database**       | In-memory database (for testing)        |
| **Maven**             | Project build & dependency management   |
| **Lombok**            | Reduces boilerplate code                |

---

## 📁 Project Structure
src
├── main
│ ├── java/com.ecommerce.E_commerce
│ │ ├── config # Security and Web config classes
│ │ ├── controller # REST/Web controllers
│ │ ├── entity # JPA entities for DB tables
│ │ ├── repository # Spring Data JPA Repositories
│ │ └── service # Business logic classes
│ └── resources
│ ├── static # CSS, JS, images
│ ├── templates # Thymeleaf HTML files
│ └── application.properties # App configuration



---

## ⚙️ Running the Application

1. Ensure **Java 17** is set in your IDE (e.g., IntelliJ SDK).
2. Clone this repository.
3. Configure **MySQL** (or use default H2 for quick run).
4. Run `ECommerceApplication.java`.
5. Access app via `http://localhost:8080`.

---
## 📦 Docker Deployment (Used for Render)

Live site : `https://e-commerce-jc55.onrender.com`
