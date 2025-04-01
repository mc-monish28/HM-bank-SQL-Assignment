package app;

import java.util.Scanner;

import bean.Account;
import bean.BankServiceProviderImpl;
import bean.Customer;

public class BankApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BankServiceProviderImpl bankService = new BankServiceProviderImpl("XYZ Bank", "123 Main St");

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Banking System Menu =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Get Account Balance");
            System.out.println("5. Transfer Money");
            System.out.println("6. Get Account Details");
            System.out.println("7. List All Accounts");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> depositMoney();
                case 3 -> withdrawMoney();
                case 4 -> getAccountBalance();
                case 5 -> transferMoney();
                case 6 -> getAccountDetails();
                case 7 -> listAllAccounts();
                case 8 -> {
                    System.out.println("Thank you for using XYZ Bank. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone Number (10 digits): ");
        String phone = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer(System.currentTimeMillis(), firstName, lastName, email, phone, address);

        System.out.println("Select Account Type:");
        System.out.println("1. Savings Account (Min Balance: 500)");
        System.out.println("2. Current Account (Overdraft Limit: 10,000)");
        System.out.println("3. Zero Balance Account");
        System.out.print("Enter choice: ");
        int accChoice = scanner.nextInt();

        float initialBalance = 0;
        if (accChoice == 1 || accChoice == 2) {
            System.out.print("Enter Initial Deposit Amount: ");
            initialBalance = scanner.nextFloat();
        }

        String accType = switch (accChoice) {
            case 1 -> "Savings";
            case 2 -> "Current";
            case 3 -> "ZeroBalance";
            default -> {
                System.out.println("Invalid choice! Account creation failed.");
                yield null;
            }
        };

        if (accType != null) {
            Account account = bankService.createAccount(customer, accType, initialBalance);
            System.out.println("Account created successfully! Account Number: " + account.getAccountNumber());
        }
    }

    private static void depositMoney() {
        System.out.print("Enter Account Number: ");
        long accNo = scanner.nextLong();
        System.out.print("Enter Deposit Amount: ");
        float amount = scanner.nextFloat();
        try {
            float newBalance = bankService.deposit(accNo, amount);
            System.out.println("Deposit successful. New Balance: " + newBalance);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void withdrawMoney() {
        System.out.print("Enter Account Number: ");
        long accNo = scanner.nextLong();
        System.out.print("Enter Withdrawal Amount: ");
        float amount = scanner.nextFloat();
        try {
            float newBalance = bankService.withdraw(accNo, amount);
            System.out.println("Withdrawal successful. New Balance: " + newBalance);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAccountBalance() {
        System.out.print("Enter Account Number: ");
        long accNo = scanner.nextLong();
        try {
            float balance = bankService.getAccountBalance(accNo);
            System.out.println("Current Balance: " + balance);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void transferMoney() {
        System.out.print("Enter Sender's Account Number: ");
        long fromAcc = scanner.nextLong();
        System.out.print("Enter Receiver's Account Number: ");
        long toAcc = scanner.nextLong();
        System.out.print("Enter Amount to Transfer: ");
        float amount = scanner.nextFloat();

        try {
            if (bankService.transfer(fromAcc, toAcc, amount)) {
                System.out.println("Transfer Successful!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAccountDetails() {
        System.out.print("Enter Account Number: ");
        long accNo = scanner.nextLong();
        try {
            System.out.println(bankService.getAccountDetails(accNo));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listAllAccounts() {
        Account[] accounts = bankService.listAccounts();
        if (accounts.length == 0) {
            System.out.println("No accounts found.");
        } else {
            for (Account acc : accounts) {
                System.out.println(acc);
                System.out.println("----------------------");
            }
        }
    }
}
