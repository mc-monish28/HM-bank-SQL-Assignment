import java.util.Scanner;

// Customer Class
class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;

    // Default Constructor
    public Customer() {
        this.customerId = 0;
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.phoneNumber = "";
        this.address = "";
    }

    // Parameterized Constructor
    public Customer(int customerId, String firstName, String lastName, String email, String phoneNumber,
            String address) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Print all information
    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Address: " + address);
    }
}

// Account Class
class Account {
    private long accountNumber;
    private String accountType;
    private double balance;

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

    // Deposit Method
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
            System.out.println("New Balance: $" + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdraw Method
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
            System.out.println("New Balance: $" + balance);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    // Calculate Interest (4.5% for Savings Account)
    public void calculateInterest() {
        if (accountType.equalsIgnoreCase("Savings")) {
            double interest = (balance * 4.5) / 100;
            balance += interest;
            System.out.println("Interest added: $" + interest);
            System.out.println("New Balance after interest: $" + balance);
        } else {
            System.out.println("Interest calculation is only applicable for Savings accounts.");
        }
    }

    // Print all account details
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Type: " + accountType);
        System.out.println("Balance: $" + balance);
    }
}

// Bank Class (Main Method)
public class Bank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Creating a Customer
        System.out.println("Enter Customer Details:");
        System.out.print("Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer(customerId, firstName, lastName, email, phone, address);

        // Display Customer Info
        System.out.println("\nCustomer Details:");
        customer.displayCustomerInfo();

        // Creating an Account
        System.out.println("\nEnter Account Details:");
        System.out.print("Account Number: ");
        long accountNumber = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        System.out.print("Account Type (Savings/Current): ");
        String accountType = scanner.nextLine();

        System.out.print("Initial Balance: ");
        double balance = scanner.nextDouble();

        Account account = new Account(accountNumber, accountType, balance);

        // Display Account Info
        System.out.println("\nAccount Details:");
        account.displayAccountInfo();

        // Banking Operations
        while (true) {
            System.out.println("\nBanking Menu:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Calculate Interest");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter amount to deposit: ");
                double amount = scanner.nextDouble();
                account.deposit(amount);
            } else if (choice == 2) {
                System.out.print("Enter amount to withdraw: ");
                double amount = scanner.nextDouble();
                account.withdraw(amount);
            } else if (choice == 3) {
                account.calculateInterest();
            } else if (choice == 4) {
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
