package service;

public interface ICustomerServiceProvider {
    float getAccountBalance(long accountNumber);

    float deposit(long accountNumber, float amount);

    float withdraw(long accountNumber, float amount);

    boolean transfer(long fromAccountNumber, long toAccountNumber, float amount);

    String getAccountDetails(long accountNumber);
}
