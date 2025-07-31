
# E-commerce Application (Spring Boot)

This is a robust E-commerce web application built using Spring Boot, focusing on core functionalities like user authentication, product management, shopping cart, wishlist, and a simulated order/payment flow.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)
- [License](#license)

## Features
- **User Authentication & Authorization**
- **Product Management**
- **Category Filtering**
- **Shopping Cart**
- **Wishlist**
- **Simulated Order Flow**
- **Order History**
- **Image Handling**
- **Thymeleaf Templates**
- **Spring Data JPA**

## Technologies Used
- Java 17
- Spring Boot 3.5.3
- Spring Security
- Maven
- MySQL
- Spring Data JPA
- Hibernate
- Thymeleaf
- HTML5 / CSS3
- Lombok

## Prerequisites
- Java JDK 8 or higher
- Maven
- MySQL database
- IDE like IntelliJ IDEA or Eclipse

## Setup and Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   cd your-repo-name
   ```

2. Configure `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.servlet.multipart.enabled=true
   spring.servlet.multipart.max-file-size=5MB
   spring.servlet.multipart.max-request-size=5MB
   ```

3. Build and run the project:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Access the app at: `http://localhost:8080`

## Usage
- Register a new user or login as an existing user.
- Add products (seller) and view them on the homepage.
- Filter products by category.
- Add to Cart / Wishlist.
- View cart, update quantity, and simulate checkout.

## Project Structure
```
com.ecommerce.E_commerce
│
├── controller
├── entity
├── repository
├── service
├── templates (Thymeleaf)
└── static/images (for product images)
```

## Future Improvements
- Payment gateway integration (e.g., Razorpay, Stripe)
- Email notifications for orders
- Admin dashboard
- Pagination & search

## Contributing
Contributions are welcome! Please fork the repository and open a pull request.

## License
This project is open-source and available under the MIT License.
