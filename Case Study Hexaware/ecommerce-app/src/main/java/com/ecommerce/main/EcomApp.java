package com.ecommerce.main;

import com.ecommerce.dao.OrderProcessorRepository;
import com.ecommerce.dao.OrderProcessorRepositoryImpl;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;

import java.util.*;

public class EcomApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderProcessorRepository repository = new OrderProcessorRepositoryImpl();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== E-Commerce Application =====");
            System.out.println("1. Register Customer");
            System.out.println("2. Create Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Add to Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Place Order");
            System.out.println("7. View Customer Orders");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        registerCustomer();
                        break;
                    case 2:
                        createProduct();
                        break;
                    case 3:
                        deleteProduct();
                        break;
                    case 4:
                        addToCart();
                        break;
                    case 5:
                        viewCart();
                        break;
                    case 6:
                        placeOrder();
                        break;
                    case 7:
                        viewCustomerOrders();
                        break;
                    case 8:
                        exit = true;
                        System.out.println("Thank you for using the E-Commerce Application!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void registerCustomer() {
        System.out.println("\n===== Register Customer =====");
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword(password);

        if (repository.createCustomer(customer)) {
            System.out.println("Customer registered successfully!");
        } else {
            System.out.println("Failed to register customer.");
        }
    }

    private static void createProduct() {
        System.out.println("\n===== Create Product =====");
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter stock quantity: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setStockQuantity(stockQuantity);

        if (repository.createProduct(product)) {
            System.out.println("Product created successfully!");
        } else {
            System.out.println("Failed to create product.");
        }
    }

    private static void deleteProduct() {
        System.out.println("\n===== Delete Product =====");
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            if (repository.deleteProduct(productId)) {
                System.out.println("Product deleted successfully!");
            }
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addToCart() {
        System.out.println("\n===== Add to Cart =====");
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Product product = new Product();
        product.setProductId(productId);

        try {
            if (repository.addToCart(customer, product, quantity)) {
                System.out.println("Product added to cart successfully!");
            }
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void viewCart() {
        System.out.println("\n===== View Cart =====");
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        try {
            List<Product> products = repository.getAllFromCart(customer);
            if (products.isEmpty()) {
                System.out.println("Cart is empty.");
            } else {
                System.out.println("Products in cart:");
                for (Product product : products) {
                    System.out.println(product);
                }
            }
        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void placeOrder() {
        System.out.println("\n===== Place Order =====");
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        List<Map<Product, Integer>> products = new ArrayList<>();
        boolean addMore = true;

        while (addMore) {
            System.out.print("Enter product ID: ");
            int productId = scanner.nextInt();
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Product product = new Product();
            product.setProductId(productId);

            Map<Product, Integer> productMap = new HashMap<>();
            productMap.put(product, quantity);
            products.add(productMap);

            System.out.print("Add another product? (y/n): ");
            String choice = scanner.nextLine();
            addMore = choice.equalsIgnoreCase("y");
        }

        System.out.print("Enter shipping address: ");
        String shippingAddress = scanner.nextLine();

        try {
            if (repository.placeOrder(customer, products, shippingAddress)) {
                System.out.println("Order placed successfully!");
            }
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void viewCustomerOrders() {
        System.out.println("\n===== View Customer Orders =====");
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            List<Map<Product, Integer>> orders = repository.getOrdersByCustomer(customerId);
            if (orders.isEmpty()) {
                System.out.println("No orders found for this customer.");
            } else {
                System.out.println("Customer Orders:");
                for (Map<Product, Integer> order : orders) {
                    for (Map.Entry<Product, Integer> entry : order.entrySet()) {
                        System.out.println("Product: " + entry.getKey().getName() +
                                ", Quantity: " + entry.getValue() +
                                ", Price: " + entry.getKey().getPrice());
                    }
                    System.out.println("-------------------");
                }
            }
        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
} 