package com.ecommerce.test;

import com.ecommerce.dao.OrderProcessorRepository;
import com.ecommerce.dao.OrderProcessorRepositoryImpl;
import com.ecommerce.exception.CustomerNotFoundException;

import java.util.Scanner;

public class CustomerDeletionDemo {
    public static void main(String[] args) {
        OrderProcessorRepository repository = new OrderProcessorRepositoryImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nCustomer Deletion Menu:");
            System.out.println("1. Delete customer by ID");
            System.out.println("2. Delete customer by name");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    deleteCustomerById(repository, scanner);
                    break;
                case 2:
                    deleteCustomerByName(repository, scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void deleteCustomerById(OrderProcessorRepository repository, Scanner scanner) {
        System.out.print("Enter customer ID to delete: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            boolean success = repository.deleteCustomer(customerId);
            if (success) {
                System.out.println("Customer with ID " + customerId + " deleted successfully!");
            } else {
                System.out.println("Failed to delete customer.");
            }
        } catch (CustomerNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteCustomerByName(OrderProcessorRepository repository, Scanner scanner) {
        System.out.print("Enter customer name to delete: ");
        String name = scanner.nextLine();

        try {
            boolean success = repository.deleteCustomerByName(name);
            if (success) {
                System.out.println("Customer '" + name + "' deleted successfully!");
            } else {
                System.out.println("Failed to delete customer.");
            }
        } catch (CustomerNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
} 