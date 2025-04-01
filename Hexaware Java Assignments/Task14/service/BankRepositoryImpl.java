package com.bank.service;

import java.util.List;

import bean.Account;
import bean.Customer;

public class BankRepositoryImpl implements IBankRepository {
    @Override
    public void createAccount(Customer customer, long accNo, String accType, float balance) {
        // Insert into database
    }

    @Override
    public List<Account> listAccounts() {
        return null; // Fetch from database
    }

    @Override
    public Account getAccountDetails(long accountNumber) {
        return null; // Fetch from database
    }

    @Override
    public float deposit(long accountNumber, float amount) {
        return 0; // Update in database
    }

    @Override
    public float withdraw(long accountNumber, float amount) {
        return 0; // Update in database
    }
}
