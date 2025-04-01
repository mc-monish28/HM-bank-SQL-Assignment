package bean;

public class CurrentAccount extends Account {
    private float overdraftLimit = 10000;

    public CurrentAccount(float balance, Customer customer) {
        super("Current", balance, customer);
    }

    public void withdraw(float amount) {
        if (balance - amount >= -overdraftLimit) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Overdraft limit exceeded.");
        }
    }

    public void deposit(float amount) {
        balance += amount;
    }
}
