import java.util.ArrayList;
import java.util.Scanner;

public class BankTransactionHistory {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> transactions = new ArrayList<>();
        double balance = 0.0;

        while (true) {
            System.out.println("\nBank Transaction Menu:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Exit and Show History");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            if (choice == 1) { // Deposit
                System.out.print("Enter deposit amount: ");
                double amount = scanner.nextDouble();
                if (amount > 0) {
                    balance += amount;
                    transactions.add("Deposited: $" + amount);
                    System.out.println("Deposit successful! Current Balance: $" + balance);
                } else {
                    System.out.println("Invalid deposit amount. Please enter a positive value.");
                }
            } else if (choice == 2) { // Withdrawal
                System.out.print("Enter withdrawal amount: ");
                double amount = scanner.nextDouble();
                if (amount > 0 && amount <= balance) {
                    balance -= amount;
                    transactions.add("Withdrawn: $" + amount);
                    System.out.println("Withdrawal successful! Current Balance: $" + balance);
                } else {
                    System.out.println("Invalid transaction. Insufficient balance or invalid amount.");
                }
            } else if (choice == 3) { // Exit and show history
                System.out.println("\nTransaction History:");
                if (transactions.isEmpty()) {
                    System.out.println("No transactions recorded.");
                } else {
                    for (String transaction : transactions) {
                        System.out.println(transaction);
                    }
                }
                System.out.println("Final Balance: $" + balance);
                break;
            } else {
                System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        scanner.close();
    }
}
