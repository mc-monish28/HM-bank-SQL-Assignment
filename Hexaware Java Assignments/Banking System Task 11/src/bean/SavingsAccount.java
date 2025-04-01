package bean;

public class SavingsAccount extends Account {
    private final float interestRate = 3.5f;
    private final float minBalance = 500;

    public SavingsAccount(float balance, Customer customer) {
        super("Savings", Math.max(balance, 500), customer);
    }

    public void withdraw(float amount) {
        if (balance - amount >= minBalance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Minimum balance of 500 required.");
        }
    }

    public void deposit(float amount) {
        balance += amount;
    }
}
