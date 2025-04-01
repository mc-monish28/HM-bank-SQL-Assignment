package com.bank.service;

import java.util.ArrayList;
import java.util.List;

import bean.Account;
import bean.CurrentAccount;
import bean.Customer;
import bean.SavingsAccount;
import bean.ZeroBalanceAccount;

public class BankServiceProviderImpl extends CustomerServiceProviderImpl implements IBankServiceProvider {
    private List<Account> accountList = new ArrayList<>();
    private String branchName;
    private String branchAddress;

    public BankServiceProviderImpl(String branchName, String branchAddress) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;
    }

    @Override
    public void createAccount(Customer customer, String accType, float balance) {
        Account account;
        if (accType.equalsIgnoreCase("Savings")) {
            account = new SavingsAccount(balance, customer, 3.5f);
        } else if (accType.equalsIgnoreCase("Current")) {
            account = new CurrentAccount(balance, customer, 10000);
        } else {
            account = new ZeroBalanceAccount(customer);
        }
        accountList.add(account);
    }

    @Override
    public List<Account> listAccounts() {
        return accountList;
    }

    @Override
    public void calculateInterest() {
        for (Account account : accountList) {
            if (account instanceof SavingsAccount) {
                float interest = ((SavingsAccount) account).getBalance() * 0.035f;
                account.deposit(interest);
            }
        }
    }
}
