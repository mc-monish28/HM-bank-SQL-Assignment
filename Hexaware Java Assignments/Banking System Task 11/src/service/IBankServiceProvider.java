package service;

import bean.Account;

public interface IBankServiceProvider {
    Account createAccount(bean.Customer customer, String accType, float balance);

    Account[] listAccounts();

    void calculateInterest();
}
