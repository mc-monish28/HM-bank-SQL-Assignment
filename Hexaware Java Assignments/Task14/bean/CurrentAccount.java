package com.bank.bean;

public class CurrentAccount extends Account {
    private float overdraftLimit;

    public CurrentAccount(float balance, Customer customer, float overdraftLimit) {
        super("Current", balance, customer);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean withdraw(float amount) {
        if (balance + overdraftLimit >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
