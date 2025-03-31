import java.util.Scanner;

public class ATMTransaction {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your current balance: ");
        double balance = scanner.nextDouble();

        System.out.println("Select an option: ");
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Your balance is: $" + balance);
                break;
            case 2:
                System.out.print("Enter withdrawal amount (multiples of 100 or 500): ");
                int withdrawAmount = scanner.nextInt();
                if (withdrawAmount % 100 == 0 || withdrawAmount % 500 == 0) {
                    if (withdrawAmount <= balance) {
                        balance -= withdrawAmount;
                        System.out.println("Withdrawal successful! Remaining balance: $" + balance);
                    } else {
                        System.out.println("Insufficient balance!");
                    }
                } else {
                    System.out.println("Invalid amount. Please enter in multiples of 100 or 500.");
                }
                break;
            case 3:
                System.out.print("Enter deposit amount: ");
                double depositAmount = scanner.nextDouble();
                balance += depositAmount;
                System.out.println("Deposit successful! New balance: $" + balance);
                break;
            default:
                System.out.println("Invalid option!");
        }

        scanner.close();
    }
}
