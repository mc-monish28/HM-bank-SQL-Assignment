package bean;

public class ZeroBalanceAccount extends Account {
    public ZeroBalanceAccount(Customer customer) {
        super("Zero Balance", 0, customer);
    }

    public void withdraw(float amount) {
        if (balance - amount >= 0) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient balance.");
        }
    }

    public void deposit(float amount) {
        balance += amount;
    }
}
