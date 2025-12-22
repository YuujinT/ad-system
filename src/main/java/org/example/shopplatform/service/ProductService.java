package org.example.shopplatform.service;

import org.example.shopplatform.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    private static List<Product> products = new ArrayList<>();

    static {
        products.add(new Product(1L, "Smartphone", "technology", "Latest smartphone with high-resolution camera", 699.99));
        products.add(new Product(2L, "Gaming Console", "gaming", "Next-gen gaming console with 4K support", 499.99));
        products.add(new Product(3L, "Travel Backpack", "travel", "Durable backpack with multiple compartments", 89.99));
        products.add(new Product(4L, "Basketball", "sports", "Official size basketball for indoor/outdoor use", 29.99));
        products.add(new Product(5L, "Gourmet Coffee Beans", "food", "Premium coffee beans from South America", 19.99));
        products.add(new Product(6L, "Laptop", "technology", "Ultra-thin laptop with long battery life", 1199.99));
        products.add(new Product(7L, "Video Game", "gaming", "Popular adventure video game", 59.99));
        products.add(new Product(8L, "Travel Guide", "travel", "Comprehensive guide to European cities", 24.99));
        products.add(new Product(9L, "Yoga Mat", "sports", "Non-slip yoga mat with carrying strap", 34.99));
        products.add(new Product(10L, "Organic Snack Box", "food", "Assorted organic snacks box", 44.99));
    }

    public static List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public static List<Product> getProductsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return getAllProducts();
        }
        return products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public static List<String> getCategories() {
        return products.stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }
}