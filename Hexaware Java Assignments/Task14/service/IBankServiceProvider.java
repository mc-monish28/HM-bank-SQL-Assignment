package com.bank.service;

import java.util.List;

import bean.Account;
import bean.Customer;

public interface IBankServiceProvider {
    void createAccount(Customer customer, String accType, float balance);

    List<Account> listAccounts();

    Account getAccountDetails(long accountNumber);

    void calculateInterest();
}
