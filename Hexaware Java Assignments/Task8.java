import java.util.Scanner;

// Base Account Class
class Account {
    protected long accountNumber;
    protected String accountType;
    protected double balance;

    // Default Constructor
    public Account() {
        this.accountNumber = 0;
        this.accountType = "";
        this.balance = 0.0;
    }

    // Parameterized Constructor
    public Account(long accountNumber, String accountType, double balance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    // Getters and Setters
    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Method Overloading: Deposit Methods
    public void deposit(float amount) {
        deposit((double) amount);
    }

    public void deposit(int amount) {
        deposit((double) amount);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
            System.out.println("New Balance: $" + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Method Overloading: Withdraw Methods
    public void withdraw(float amount) {
        withdraw((double) amount);
    }

    public void withdraw(int amount) {
        withdraw((double) amount);
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
            System.out.println("New Balance: $" + balance);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    // Placeholder for calculating interest
    public void calculateInterest() {
        System.out.println("No interest calculation for this account type.");
    }

    // Display Account Details
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Type: " + accountType);
        System.out.println("Balance: $" + balance);
    }
}

// Subclass: Savings Account
class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 4.5;

    public SavingsAccount(long accountNumber, double balance) {
        super(accountNumber, "Savings", balance);
    }

    // Override calculateInterest() for SavingsAccount
    @Override
    public void calculateInterest() {
        double interest = (balance * INTEREST_RATE) / 100;
        balance += interest;
        System.out.println("Interest added: $" + interest);
        System.out.println("New Balance after interest: $" + balance);
    }
}

// Subclass: Current Account
class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 1000.0;

    public CurrentAccount(long accountNumber, double balance) {
        super(accountNumber, "Current", balance);
    }

    // Override withdraw() for CurrentAccount
    @Override
    public void withdraw(double amount) {
        if (amount > 0 && (balance - amount >= -OVERDRAFT_LIMIT)) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
            System.out.println("New Balance: $" + balance);
        } else {
            System.out.println("Overdraft limit exceeded! Transaction denied.");
        }
    }
}

// Bank Class (Main Method)
public class Bank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Account account = null;

        System.out.println("Choose Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        System.out.print("Enter Account Number: ");
        long accountNumber = scanner.nextLong();

        System.out.print("Enter Initial Balance: ");
        double initialBalance = scanner.nextDouble();

        switch (choice) {
            case 1:
                account = new SavingsAccount(accountNumber, initialBalance);
                break;
            case 2:
                account = new CurrentAccount(accountNumber, initialBalance);
                break;
            default:
                System.out.println("Invalid choice! Exiting...");
                return;
        }

        System.out.println("\nAccount Created Successfully!");
        account.displayAccountInfo();

        // Banking Operations Menu
        while (true) {
            System.out.println("\nBanking Menu:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Calculate Interest (For Savings Account)");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int action = scanner.nextInt();

            if (action == 1) {
                System.out.print("Enter amount to deposit: ");
                double amount = scanner.nextDouble();
                account.deposit(amount);
            } else if (action == 2) {
                System.out.print("Enter amount to withdraw: ");
                double amount = scanner.nextDouble();
                account.withdraw(amount);
            } else if (action == 3) {
                account.calculateInterest();
            } else if (action == 4) {
                System.out.println("\nFinal Account Details:");
                account.displayAccountInfo();
                System.out.println("Thank you for banking with us!");
                break;
            } else {
                System.out.println("Invalid choice. Please select again.");
            }
        }

        scanner.close();
    }
}
