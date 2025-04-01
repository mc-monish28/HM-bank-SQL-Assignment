import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// ===================== Custom Exceptions ===================== //
class InsufficientFundException extends Exception {
    public InsufficientFundException(String message) {
        super(message);
    }
}

class InvalidAccountException extends Exception {
    public InvalidAccountException(String message) {
        super(message);
    }
}

class OverDraftLimitExceededException extends Exception {
    public OverDraftLimitExceededException(String message) {
        super(message);
    }
}

// ===================== Customer Class ===================== //
class Customer {
    private String name;
    private String email;
    private String phone;

    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Customer{name='" + name + "', email='" + email + "', phone='" + phone + "'}";
    }
}

// ===================== Account Base Class ===================== //
abstract class Account {
    private static long lastAccNo = 1000;
    protected long accountNumber;
    protected String accountType;
    protected float balance;
    protected Customer customer;

    public Account(Customer customer, String accountType, float balance) {
        this.accountNumber = ++lastAccNo;
        this.accountType = accountType;
        this.balance = balance;
        this.customer = customer;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public abstract void withdraw(float amount) throws InsufficientFundException, OverDraftLimitExceededException;

    @Override
    public String toString() {
        return "Account{accountNumber=" + accountNumber + ", accountType='" + accountType + "', balance=" + balance
                + ", customer=" + customer + "}";
    }
}

// ===================== SavingsAccount Class ===================== //
class SavingsAccount extends Account {
    public static final float MIN_BALANCE = 500;

    public SavingsAccount(Customer customer, float balance) {
        super(customer, "Savings", Math.max(balance, MIN_BALANCE));
    }

    @Override
    public void withdraw(float amount) throws InsufficientFundException {
        if (balance - amount < MIN_BALANCE) {
            throw new InsufficientFundException("Cannot withdraw! Minimum balance of ₹500 required.");
        }
        balance -= amount;
    }
}

// ===================== CurrentAccount Class ===================== //
class CurrentAccount extends Account {
    private float overdraftLimit = 5000;

    public CurrentAccount(Customer customer, float balance) {
        super(customer, "Current", balance);
    }

    @Override
    public void withdraw(float amount) throws OverDraftLimitExceededException {
        if (balance + overdraftLimit < amount) {
            throw new OverDraftLimitExceededException("Overdraft limit exceeded!");
        }
        balance -= amount;
    }
}

// ===================== ZeroBalanceAccount Class ===================== //
class ZeroBalanceAccount extends Account {
    public ZeroBalanceAccount(Customer customer) {
        super(customer, "ZeroBalance", 0);
    }

    @Override
    public void withdraw(float amount) throws InsufficientFundException {
        if (balance < amount) {
            throw new InsufficientFundException("Cannot withdraw! No sufficient balance.");
        }
        balance -= amount;
    }
}

// ===================== Service Interfaces ===================== //
interface ICustomerServiceProvider {
    float getAccountBalance(long accNo) throws InvalidAccountException;

    float deposit(long accNo, float amount) throws InvalidAccountException;

    float withdraw(long accNo, float amount)
            throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException;

    boolean transfer(long fromAccNo, long toAccNo, float amount)
            throws InvalidAccountException, InsufficientFundException;

    Account getAccountDetails(long accNo) throws InvalidAccountException;
}

interface IBankServiceProvider {
    Account createAccount(Customer customer, String accType, float balance);

    Collection<Account> listAccounts();
}

// ===================== BankServiceProviderImpl Class ===================== //
class BankServiceProviderImpl implements ICustomerServiceProvider, IBankServiceProvider {
    // ** Uncomment one of the below implementations at a time **

    // List<Account> accounts = new ArrayList<>();
    // Set<Account> accounts = new TreeSet<>(Comparator.comparing(a ->
    // a.getCustomer().getName()));
    Map<Long, Account> accounts = new HashMap<>();

    public Account createAccount(Customer customer, String accType, float balance) {
        Account account;
        switch (accType) {
            case "Savings" -> account = new SavingsAccount(customer, balance);
            case "Current" -> account = new CurrentAccount(customer, balance);
            case "ZeroBalance" -> account = new ZeroBalanceAccount(customer);
            default -> throw new IllegalArgumentException("Invalid account type!");
        }
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    public Account findAccount(long accNo) {
        return accounts.get(accNo);
    }

    public float deposit(long accNo, float amount) throws InvalidAccountException {
        Account account = findAccount(accNo);
        if (account == null)
            throw new InvalidAccountException("Account not found!");
        account.setBalance(account.getBalance() + amount);
        return account.getBalance();
    }

    public float withdraw(long accNo, float amount)
            throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException {
        Account account = findAccount(accNo);
        if (account == null)
            throw new InvalidAccountException("Account not found!");
        account.withdraw(amount);
        return account.getBalance();
    }

    public boolean transfer(long fromAccNo, long toAccNo, float amount)
            throws InvalidAccountException, InsufficientFundException {
        Account fromAccount = findAccount(fromAccNo);
        Account toAccount = findAccount(toAccNo);

        if (fromAccount == null)
            throw new InvalidAccountException("Sender account not found!");
        if (toAccount == null)
            throw new InvalidAccountException("Receiver account not found!");
        if (fromAccount.getBalance() < amount)
            throw new InsufficientFundException("Insufficient funds!");

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        return true;
    }

    public float getAccountBalance(long accNo) throws InvalidAccountException {
        Account account = findAccount(accNo);
        if (account == null)
            throw new InvalidAccountException("Account not found!");
        return account.getBalance();
    }

    public Account getAccountDetails(long accNo) throws InvalidAccountException {
        Account account = findAccount(accNo);
        if (account == null)
            throw new InvalidAccountException("Account not found!");
        return account;
    }

    public Collection<Account> listAccounts() {
        return accounts.values();
    }
}

// ===================== BankApp Main Class ===================== //
public class BankApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BankServiceProviderImpl bankService = new BankServiceProviderImpl();

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("\n===== Banking System Menu =====");
                System.out.println("1. Create Account");
                System.out.println("2. Deposit Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. Get Account Balance");
                System.out.println("5. Transfer Money");
                System.out.println("6. Get Account Details");
                System.out.println("7. List All Accounts");
                System.out.println("8. Exit");
                System.out.print("Enter choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> bankService.createAccount(new Customer("John", "john@example.com", "9999999999"),
                            "Savings", 1000);
                    case 2 -> bankService.deposit(1001, 500);
                    case 3 -> bankService.withdraw(1001, 200);
                    case 4 -> System.out.println("Balance: " + bankService.getAccountBalance(1001));
                    case 5 -> bankService.transfer(1001, 1002, 100);
                    case 6 -> System.out.println(bankService.getAccountDetails(1001));
                    case 7 -> System.out.println(bankService.listAccounts());
                    case 8 -> System.exit(0);
                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
