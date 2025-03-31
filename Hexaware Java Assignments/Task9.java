import java.util.Scanner;

// Abstract Class: BankAccount
abstract class BankAccount {
    protected long accountNumber;
    protected String customerName;
    protected double balance;

    // Default Constructor
    public BankAccount() {
        this.accountNumber = 0;
        this.customerName = "";
        this.balance = 0.0;
    }

    // Parameterized Constructor
    public BankAccount(long accountNumber, String customerName, double balance) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
    }

    // Getters and Setters
    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Abstract Methods
    public abstract void deposit(float amount);

    public abstract void withdraw(float amount);

    public abstract void calculateInterest();

    // Display Account Details
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Balance: $" + balance);
    }
}

// Concrete Class: SavingsAccount
class SavingsAccount extends BankAccount {
    private static final double INTEREST_RATE = 4.5;

    // Constructor
    public SavingsAccount(long accountNumber, String customerName, double balance) {
        super(accountNumber, customerName, balance);
    }

    // Deposit Method
    @Override
    public void deposit(float amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
            System.out.println("New Balance: $" + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdraw Method
    @Override
    public void withdraw(float amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
            System.out.println("New Balance: $" + balance);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    // Calculate Interest
    @Override
    public void calculateInterest() {
        double interest = (balance * INTEREST_RATE) / 100;
        balance += interest;
        System.out.println("Interest added: $" + interest);
        System.out.println("New Balance after interest: $" + balance);
    }
}

// Concrete Class: CurrentAccount
class CurrentAccount extends BankAccount {
    private static final double OVERDRAFT_LIMIT = 1000.0;

    // Constructor
    public CurrentAccount(long accountNumber, String customerName, double balance) {
        super(accountNumber, customerName, balance);
    }

    // Deposit Method
    @Override
    public void deposit(float amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
            System.out.println("New Balance: $" + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdraw Method with Overdraft
    @Override
    public void withdraw(float amount) {
        if (amount > 0 && (balance - amount >= -OVERDRAFT_LIMIT)) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
            System.out.println("New Balance: $" + balance);
        } else {
            System.out.println("Overdraft limit exceeded! Transaction denied.");
        }
    }

    // No Interest Calculation for Current Account
    @Override
    public void calculateInterest() {
        System.out.println("Current Accounts do not have interest calculation.");
    }
}

// Bank Class (Main Method)
public class Bank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankAccount account = null;

        System.out.println("Choose Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        System.out.print("Enter Account Number: ");
        long accountNumber = scanner.nextLong();

        scanner.nextLine(); // Consume newline
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter Initial Balance: ");
        double initialBalance = scanner.nextDouble();

        switch (choice) {
            case 1:
                account = new SavingsAccount(accountNumber, customerName, initialBalance);
                break;
            case 2:
                account = new CurrentAccount(accountNumber, customerName, initialBalance);
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
                float amount = scanner.nextFloat();
                account.deposit(amount);
            } else if (action == 2) {
                System.out.print("Enter amount to withdraw: ");
                float amount = scanner.nextFloat();
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
