import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Customer Class (Represents the Customer)
class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;

    // Static variable to auto-increment Customer ID
    private static int idCounter = 100;

    // Default Constructor
    public Customer() {
        this.customerID = idCounter++;
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.phoneNumber = "";
        this.address = "";
    }

    // Parameterized Constructor
    public Customer(String firstName, String lastName, String email, String phoneNumber, String address) {
        this.customerID = idCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
        setEmail(email);
        setPhoneNumber(phoneNumber);
        this.address = address;
    }

    // Getters
    public int getCustomerID() {
        return customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    // Setters with Validation
    public void setEmail(String email) {
        if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\d{10}")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number. It should be 10 digits.");
        }
    }

    // Display Customer Details
    public void displayCustomerInfo() {
        System.out.println("\nCustomer ID: " + customerID);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Address: " + address);
    }
}

// Account Class (Represents a Bank Account)
class Account {
    private long accountNumber;
    private String accountType;
    private double balance;
    private Customer customer;

    // Static variable for auto-incrementing Account Numbers
    private static long accountCounter = 1001;

    // Parameterized Constructor
    public Account(Customer customer, String accountType, double initialBalance) {
        this.accountNumber = accountCounter++;
        this.customer = customer;
        this.accountType = accountType;
        this.balance = initialBalance;
    }

    // Getters
    public long getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    // Deposit Method
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited $" + amount + ". New Balance: $" + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdraw Method
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn $" + amount + ". New Balance: $" + balance);
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }

    // Display Account Details
    public void displayAccountInfo() {
        System.out.println("\nAccount Number: " + accountNumber);
        System.out.println("Account Type: " + accountType);
        System.out.println("Balance: $" + balance);
        customer.displayCustomerInfo();
    }
}

// Bank Class (Handles Banking Operations)
class Bank {
    private Map<Long, Account> accounts = new HashMap<>();

    // Create Account
    public Account createAccount(Customer customer, String accType, double initialBalance) {
        Account newAccount = new Account(customer, accType, initialBalance);
        accounts.put(newAccount.getAccountNumber(), newAccount);
        System.out.println("\nAccount Created Successfully! Account Number: " + newAccount.getAccountNumber());
        return newAccount;
    }

    // Get Account Balance
    public double getAccountBalance(long accNo) {
        Account acc = accounts.get(accNo);
        return (acc != null) ? acc.getBalance() : -1;
    }

    // Deposit Money
    public void deposit(long accNo, double amount) {
        Account acc = accounts.get(accNo);
        if (acc != null) {
            acc.deposit(amount);
        } else {
            System.out.println("Account not found.");
        }
    }

    // Withdraw Money
    public void withdraw(long accNo, double amount) {
        Account acc = accounts.get(accNo);
        if (acc != null) {
            acc.withdraw(amount);
        } else {
            System.out.println("Account not found.");
        }
    }

    // Transfer Money
    public void transfer(long fromAccNo, long toAccNo, double amount) {
        Account fromAcc = accounts.get(fromAccNo);
        Account toAcc = accounts.get(toAccNo);

        if (fromAcc != null && toAcc != null) {
            if (fromAcc.getBalance() >= amount) {
                fromAcc.withdraw(amount);
                toAcc.deposit(amount);
                System.out.println("Transferred $" + amount + " from Account " + fromAccNo + " to Account " + toAccNo);
            } else {
                System.out.println("Insufficient balance for transfer.");
            }
        } else {
            System.out.println("One or both accounts not found.");
        }
    }

    // Get Account Details
    public void getAccountDetails(long accNo) {
        Account acc = accounts.get(accNo);
        if (acc != null) {
            acc.displayAccountInfo();
        } else {
            System.out.println("Account not found.");
        }
    }
}

// BankApp (Main Class)
public class BankApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("\nBanking System Menu:");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Get Balance");
            System.out.println("6. Get Account Details");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter First Name: ");
                    String fName = scanner.nextLine();
                    System.out.print("Enter Last Name: ");
                    String lName = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter Address: ");
                    String address = scanner.nextLine();

                    Customer newCustomer = new Customer(fName, lName, email, phone, address);

                    System.out.print("Enter Account Type (Savings/Current): ");
                    String accType = scanner.nextLine();
                    System.out.print("Enter Initial Balance: ");
                    double balance = scanner.nextDouble();

                    bank.createAccount(newCustomer, accType, balance);
                    break;

                case 2:
                    System.out.print("Enter Account Number: ");
                    long accNo = scanner.nextLong();
                    System.out.print("Enter Deposit Amount: ");
                    double depositAmount = scanner.nextDouble();
                    bank.deposit(accNo, depositAmount);
                    break;

                case 3:
                    System.out.print("Enter Account Number: ");
                    accNo = scanner.nextLong();
                    System.out.print("Enter Withdrawal Amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    bank.withdraw(accNo, withdrawAmount);
                    break;

                case 4:
                    System.out.print("Enter From Account Number: ");
                    long fromAcc = scanner.nextLong();
                    System.out.print("Enter To Account Number: ");
                    long toAcc = scanner.nextLong();
                    System.out.print("Enter Amount to Transfer: ");
                    double amount = scanner.nextDouble();
                    bank.transfer(fromAcc, toAcc, amount);
                    break;

                case 5:
                    System.out.print("Enter Account Number: ");
                    accNo = scanner.nextLong();
                    System.out.println("Current Balance: $" + bank.getAccountBalance(accNo));
                    break;

                case 6:
                    System.out.print("Enter Account Number: ");
                    accNo = scanner.nextLong();
                    bank.getAccountDetails(accNo);
                    break;

                case 7:
                    System.out.println("Exiting Banking System...");
                    scanner.close();
                    return;
            }
        }
    }
}
