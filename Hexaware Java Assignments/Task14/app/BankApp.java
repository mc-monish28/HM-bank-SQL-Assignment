package com.bank.app;

import java.util.Scanner;

import bean.Customer;
import service.BankRepositoryImpl;

public class BankApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankRepositoryImpl bank = new BankRepositoryImpl();

        while (true) {
            System.out.println("1. Create Account\n2. Deposit\n3. Get Balance\n4. Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter customer ID: ");
                    long custId = sc.nextLong();
                    sc.nextLine();
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter account number: ");
                    long accNo = sc.nextLong();
                    System.out.print("Enter account type (Savings/Current): ");
                    sc.nextLine();
                    String accType = sc.nextLine();
                    System.out.print("Enter initial balance: ");
                    float balance = sc.nextFloat();

                    Customer customer = new Customer(custId, name, email, phone);
                    bank.createAccount(customer, accNo, accType, balance);
                    System.out.println("Account created successfully.");
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    accNo = sc.nextLong();
                    System.out.print("Enter amount to deposit: ");
                    float amount = sc.nextFloat();
                    float newBalance = bank.deposit(accNo, amount);
                    System.out.println("New Balance: " + newBalance);
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    accNo = sc.nextLong();
                    System.out.println("Balance: " + bank.getAccountBalance(accNo));
                    break;
                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
            }
        }
    }
}
