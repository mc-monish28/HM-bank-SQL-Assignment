package com.order.management.main;

import com.order.management.dao.IOrderManagementRepository;
import com.order.management.dao.OrderProcessor;
import com.order.management.entity.Clothing;
import com.order.management.entity.Electronics;
import com.order.management.entity.Product;
import com.order.management.entity.User;
import com.order.management.exception.OrderNotFoundException;
import com.order.management.exception.UserNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static final Scanner scanner = new Scanner(System.in);
    private static final IOrderManagementRepository orderProcessor = new OrderProcessor();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\nOrder Management System");
            System.out.println("1. Create User");
            System.out.println("2. Create Product");
            System.out.println("3. Create Order");
            System.out.println("4. Cancel Order");
            System.out.println("5. Get All Products");
            System.out.println("6. Get Orders by User");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        createProduct();
                        break;
                    case 3:
                        createOrder();
                        break;
                    case 4:
                        cancelOrder();
                        break;
                    case 5:
                        getAllProducts();
                        break;
                    case 6:
                        getOrdersByUser();
                        break;
                    case 7:
                        running = false;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void createUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (Admin/User): ");
        String role = scanner.nextLine();

        User user = new User(0, username, password, role);
        orderProcessor.createUser(user);
        System.out.println("User created successfully!");
    }

    private static void createProduct() {
        System.out.print("Enter admin user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter product type (Electronics/Clothing): ");
        String type = scanner.nextLine();

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter quantity in stock: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Product product;
        if ("Electronics".equalsIgnoreCase(type)) {
            System.out.print("Enter brand: ");
            String brand = scanner.nextLine();
            System.out.print("Enter warranty period (months): ");
            int warranty = scanner.nextInt();
            scanner.nextLine();

            product = new Electronics(0, name, description, price, quantity, brand, warranty);
        } else {
            System.out.print("Enter size: ");
            String size = scanner.nextLine();
            System.out.print("Enter color: ");
            String color = scanner.nextLine();

            product = new Clothing(0, name, description, price, quantity, size, color);
        }

        try {
            orderProcessor.createProduct(new User(userId, "", "", "Admin"), product);
            System.out.println("Product created successfully!");
        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createOrder() {
        try {
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            scanner.nextLine();

            List<Product> products = new ArrayList<>();
            boolean addingProducts = true;
            boolean hasInvalidProduct = false;
            
            while (addingProducts) {
                System.out.print("Enter product ID (or 0 to finish): ");
                int productId = scanner.nextInt();
                scanner.nextLine();

                if (productId == 0) {
                    addingProducts = false;
                } else {
                    // Check if product exists before adding to list
                    try {
                        if (!orderProcessor.isProductExists(productId)) {
                            System.out.println("Error: Product with ID " + productId + " does not exist");
                            hasInvalidProduct = true;
                            continue;
                        }
                        Product product = new Product();
                        product.setProductId(productId);
                        products.add(product);
                    } catch (SQLException e) {
                        System.out.println("Error checking product: " + e.getMessage());
                        hasInvalidProduct = true;
                    }
                }
            }

            if (hasInvalidProduct) {
                System.out.println("Order creation cancelled due to invalid products.");
                return;
            }

            if (products.isEmpty()) {
                System.out.println("No products added to the order. Order creation cancelled.");
                return;
            }

            try {
                orderProcessor.createOrder(new User(userId, "", "", ""), products);
                System.out.println("Order created successfully!");
            } catch (UserNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Error creating order: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error: Invalid input. Please try again.");
            scanner.nextLine(); // Clear the scanner buffer
        }
    }

    private static void cancelOrder() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();

        try {
            orderProcessor.cancelOrder(userId, orderId);
            System.out.println("Order cancelled successfully!");
        } catch (UserNotFoundException | OrderNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getAllProducts() {
        List<Product> products = orderProcessor.getAllProducts();
        System.out.println("\nAll Products:");
        for (Product product : products) {
            System.out.println("ID: " + product.getProductId());
            System.out.println("Name: " + product.getProductName());
            System.out.println("Type: " + product.getType());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Quantity: " + product.getQuantityInStock());
            
            if (product instanceof Electronics) {
                Electronics electronics = (Electronics) product;
                System.out.println("Brand: " + electronics.getBrand());
                System.out.println("Warranty: " + electronics.getWarrantyPeriod() + " months");
            } else if (product instanceof Clothing) {
                Clothing clothing = (Clothing) product;
                System.out.println("Size: " + clothing.getSize());
                System.out.println("Color: " + clothing.getColor());
            }
            System.out.println("-------------------");
        }
    }

    private static void getOrdersByUser() {
        try {
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();

            List<Product> products = orderProcessor.getOrderByUser(new User(userId, "", "", ""));
            
            if (products.isEmpty()) {
                System.out.println("\nUser " + userId + " has no orders.");
            } else {
                System.out.println("\nUser " + userId + " has " + products.size() + " order(s):");
                System.out.println("----------------------------------------");
                for (Product product : products) {
                    System.out.println("Product ID: " + product.getProductId());
                    System.out.println("Name: " + product.getProductName());
                    System.out.println("Type: " + product.getType());
                    System.out.println("Price: $" + product.getPrice());
                    System.out.println("Description: " + product.getDescription());
                    
                    if (product instanceof Electronics) {
                        Electronics electronics = (Electronics) product;
                        System.out.println("Brand: " + electronics.getBrand());
                        System.out.println("Warranty Period: " + electronics.getWarrantyPeriod() + " months");
                    } else if (product instanceof Clothing) {
                        Clothing clothing = (Clothing) product;
                        System.out.println("Size: " + clothing.getSize());
                        System.out.println("Color: " + clothing.getColor());
                    }
                    System.out.println("----------------------------------------");
                }
            }
        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: Invalid input. Please try again.");
            scanner.nextLine(); // Clear the scanner buffer
        }
    }
} 