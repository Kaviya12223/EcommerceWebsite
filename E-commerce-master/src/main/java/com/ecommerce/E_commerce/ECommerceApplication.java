package com.ecommerce.E_commerce;

import com.ecommerce.E_commerce.entity.Category;
import com.ecommerce.E_commerce.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ECommerceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Bean
	CommandLineRunner run(CategoryRepository categoryRepository) {
		return args -> {
			if (categoryRepository.count() == 0) {
				List<String> categoryNames = List.of(
						"Electronics", "Fashion", "Home & Kitchen", "Books", "Toys & Games",
						"Beauty & Personal Care", "Sports & Outdoors", "Automotive", "Grocery",
						"Health & Wellness", "Mobile Phones", "Laptops", "Furniture", "Footwear",
						"Watches", "Jewelry", "Stationery", "Music & Instruments", "Pet Supplies", "Baby Products"
				);

				for (String name : categoryNames) {
					Category category = new Category();
					category.setName(name);
					categoryRepository.save(category);
				}
			}
		};
	}
}
