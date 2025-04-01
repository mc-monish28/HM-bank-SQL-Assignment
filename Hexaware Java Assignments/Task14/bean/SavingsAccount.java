package com.bank.bean;

public class SavingsAccount extends Account {
    private float interestRate;
    private static final float MIN_BALANCE = 500;

    public SavingsAccount(float balance, Customer customer, float interestRate) {
        super("Savings", Math.max(balance, MIN_BALANCE), customer);
        this.interestRate = interestRate;
    }

    @Override
    public boolean withdraw(float amount) {
        if (balance - amount >= MIN_BALANCE) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
