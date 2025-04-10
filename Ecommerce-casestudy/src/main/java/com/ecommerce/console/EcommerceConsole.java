package com.ecommerce.console;

import com.ecommerce.dao.OrderProcessorRepository;
import com.ecommerce.dao.OrderProcessorRepositoryImpl;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class EcommerceConsole {
    private final OrderProcessorRepository repository;
    private final Scanner scanner;

    public EcommerceConsole() {
        this.repository = new OrderProcessorRepositoryImpl();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== E-commerce System ===");
            System.out.println("1. Customer Management");
            System.out.println("2. Product Management");
            System.out.println("3. Cart Management");
            System.out.println("4. Order Management");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    customerManagement();
                    break;
                case 2:
                    productManagement();
                    break;
                case 3:
                    cartManagement();
                    break;
                case 4:
                    orderManagement();
                    break;
                case 5:
                    System.out.println("Thank you for using the E-commerce System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void customerManagement() {
        while (true) {
            System.out.println("\n=== Customer Management ===");
            System.out.println("1. Create Customer");
            System.out.println("2. Delete Customer by ID");
            System.out.println("3. Delete Customer by Name");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createCustomer();
                    break;
                case 2:
                    deleteCustomerById();
                    break;
                case 3:
                    deleteCustomerByName();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void deleteCustomerById() {
        try {
            System.out.print("Enter Customer ID to delete: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (repository.deleteCustomer(customerId)) {
                System.out.println("Customer deleted successfully!");
            } else {
                System.out.println("Failed to delete customer.");
            }
        } catch (CustomerNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteCustomerByName() {
        try {
            System.out.print("Enter Customer Name to delete: ");
            String name = scanner.nextLine();

            if (repository.deleteCustomerByName(name)) {
                System.out.println("Customer deleted successfully!");
            } else {
                System.out.println("Failed to delete customer.");
            }
        } catch (CustomerNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cartManagement() {
        while (true) {
            System.out.println("\n=== Cart Management ===");
            System.out.println("1. Add to Cart");
            System.out.println("2. Remove from Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addToCart();
                    break;
                case 2:
                    removeFromCart();
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void removeFromCart() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Product ID: ");
            int productId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Customer customer = new Customer();
            customer.setCustomerId(customerId);

            Product product = new Product();
            product.setProductId(productId);

            if (repository.removeFromCart(customer, product)) {
                System.out.println("Product removed from cart successfully!");
            } else {
                System.out.println("Failed to remove product from cart.");
            }
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void productManagement() {
        while (true) {
            System.out.println("\n=== Product Management ===");
            System.out.println("1. Create Product");
            System.out.println("2. Delete Product");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice (1-3): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        createProduct();
                        break;
                    case 2:
                        deleteProduct();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private void createProduct() {
        try {
            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Product Price: ");
            double price = 0;
            while (true) {
                try {
                    price = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    if (price > 0) {
                        break;
                    } else {
                        System.out.println("Price must be greater than 0. Please try again:");
                    }
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid price:");
                    scanner.nextLine(); // Clear the invalid input
                }
            }

            System.out.print("Enter Product Description: ");
            String description = scanner.nextLine();

            System.out.print("Enter Stock Quantity: ");
            int stockQuantity = 0;
            while (true) {
                try {
                    stockQuantity = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (stockQuantity >= 0) {
                        break;
                    } else {
                        System.out.println("Stock quantity cannot be negative. Please try again:");
                    }
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid quantity:");
                    scanner.nextLine(); // Clear the invalid input
                }
            }

            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setStockQuantity(stockQuantity);

            if (repository.createProduct(product)) {
                System.out.println("Product created successfully with ID: " + product.getProductId());
            } else {
                System.out.println("Failed to create product.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        try {
            System.out.print("Enter Product ID to delete: ");
            int productId = 0;
            while (true) {
                try {
                    productId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (productId > 0) {
                        break;
                    } else {
                        System.out.println("Product ID must be greater than 0. Please try again:");
                    }
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid product ID:");
                    scanner.nextLine(); // Clear the invalid input
                }
            }

            if (repository.deleteProduct(productId)) {
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("Failed to delete product.");
            }
        } catch (ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createCustomer() {
        try {
            System.out.print("Enter Customer Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);
            customer.setPassword(password);

            if (repository.createCustomer(customer)) {
                System.out.println("Customer created successfully with ID: " + customer.getCustomerId());
            } else {
                System.out.println("Failed to create customer.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addToCart() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Product ID: ");
            int productId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Customer customer = new Customer();
            customer.setCustomerId(customerId);

            Product product = new Product();
            product.setProductId(productId);

            if (repository.addToCart(customer, product, quantity)) {
                System.out.println("Product added to cart successfully!");
            } else {
                System.out.println("Failed to add product to cart.");
            }
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewCart() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Customer customer = new Customer();
            customer.setCustomerId(customerId);

            List<Product> cartItems = repository.getAllFromCart(customer);
            if (cartItems.isEmpty()) {
                System.out.println("Cart is empty.");
            } else {
                System.out.println("\nCart Items:");
                for (Product product : cartItems) {
                    System.out.println("ID: " + product.getProductId() +
                            ", Name: " + product.getName() +
                            ", Price: " + product.getPrice() +
                            ", Description: " + product.getDescription());
                }
            }
        } catch (CustomerNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void orderManagement() {
        while (true) {
            System.out.println("\n=== Order Management ===");
            System.out.println("1. Place Order");
            System.out.println("2. View Orders");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    placeOrder();
                    break;
                case 2:
                    viewOrders();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void placeOrder() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Customer customer = new Customer();
            customer.setCustomerId(customerId);

            System.out.print("Enter Shipping Address: ");
            String shippingAddress = scanner.nextLine();

            // Get cart items
            List<Product> cartItems = repository.getAllFromCart(customer);
            if (cartItems.isEmpty()) {
                System.out.println("Cart is empty. Cannot place order.");
                return;
            }

            // Convert cart items to order format
            List<Map<Product, Integer>> orderItems = new ArrayList<>();
            Map<Product, Integer> productMap = new HashMap<>();
            for (Product product : cartItems) {
                productMap.put(product, 1); // Assuming quantity 1 for each item
            }
            orderItems.add(productMap);

            if (repository.placeOrder(customer, orderItems, shippingAddress)) {
                System.out.println("Order placed successfully!");
            } else {
                System.out.println("Failed to place order.");
            }
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewOrders() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            List<Map<Product, Integer>> orders = repository.getOrdersByCustomer(customerId);
            if (orders.isEmpty()) {
                System.out.println("No orders found.");
            } else {
                System.out.println("\nOrders:");
                for (Map<Product, Integer> order : orders) {
                    System.out.println("Order Items:");
                    for (Map.Entry<Product, Integer> entry : order.entrySet()) {
                        Product product = entry.getKey();
                        int quantity = entry.getValue();
                        System.out.println("  Product: " + product.getName() +
                                ", Quantity: " + quantity +
                                ", Price: " + product.getPrice());
                    }
                    System.out.println("-------------------");
                }
            }
        } catch (CustomerNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        EcommerceConsole console = new EcommerceConsole();
        console.start();
    }
} 