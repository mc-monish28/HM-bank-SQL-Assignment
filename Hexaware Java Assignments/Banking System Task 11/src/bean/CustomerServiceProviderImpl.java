package bean;

import java.util.HashMap;

import service.ICustomerServiceProvider;

public class CustomerServiceProviderImpl implements ICustomerServiceProvider {
    protected HashMap<Long, Account> accounts = new HashMap<>();

    public float getAccountBalance(long accountNumber) {
        if (!accounts.containsKey(accountNumber))
            throw new IllegalArgumentException("Account not found.");
        return accounts.get(accountNumber).getBalance();
    }

    public float deposit(long accountNumber, float amount) {
        if (!accounts.containsKey(accountNumber))
            throw new IllegalArgumentException("Account not found.");
        accounts.get(accountNumber).deposit(amount);
        return accounts.get(accountNumber).getBalance();
    }

    public float withdraw(long accountNumber, float amount) {
        if (!accounts.containsKey(accountNumber))
            throw new IllegalArgumentException("Account not found.");
        accounts.get(accountNumber).withdraw(amount);
        return accounts.get(accountNumber).getBalance();
    }

    public boolean transfer(long fromAcc, long toAcc, float amount) {
        withdraw(fromAcc, amount);
        deposit(toAcc, amount);
        return true;
    }

    public String getAccountDetails(long accountNumber) {
        if (!accounts.containsKey(accountNumber))
            throw new IllegalArgumentException("Account not found.");
        return accounts.get(accountNumber).toString();
    }
}
