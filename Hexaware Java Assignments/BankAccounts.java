import java.util.HashMap;
import java.util.Scanner;

public class BankAccounts {
    public static void main(String[] args) {
        // Sample customer accounts (account number -> balance)
        HashMap<Integer, Double> accounts = new HashMap<>();
        accounts.put(101, 5000.0);
        accounts.put(102, 7500.5);
        accounts.put(103, 12000.0);
        accounts.put(104, 3000.75);

        Scanner scanner = new Scanner(System.in);
        int accountNumber;

        while (true) {
            System.out.print("Enter your account number: ");
            accountNumber = scanner.nextInt();

            if (accounts.containsKey(accountNumber)) {
                System.out.println("Your account balance: $" + accounts.get(accountNumber));
                break;
            } else {
                System.out.println("Invalid account number. Please try again.");
            }
        }

        scanner.close();
    }
}
