package bean;

public abstract class Account {
    protected static long lastAccNo = 1000;
    protected long accountNumber;
    protected String accountType;
    protected float balance;
    protected Customer customer;

    public Account(String accountType, float balance, Customer customer) {
        this.accountNumber = ++lastAccNo;
        this.accountType = accountType;
        this.balance = balance;
        this.customer = customer;
    }

    public abstract void withdraw(float amount);

    public abstract void deposit(float amount);

    public long getAccountNumber() {
        return accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public String toString() {
        return "Account No: " + accountNumber + "\nType: " + accountType + "\nBalance: " + balance + "\nCustomer: "
                + customer;
    }
}
