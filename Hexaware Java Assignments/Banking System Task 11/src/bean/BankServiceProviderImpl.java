package bean;

import service.IBankServiceProvider;

public class BankServiceProviderImpl extends CustomerServiceProviderImpl implements IBankServiceProvider {
    private String branchName;
    private String branchAddress;

    public BankServiceProviderImpl(String branchName, String branchAddress) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;
    }

    public Account createAccount(Customer customer, String accType, float balance) {
        Account acc = switch (accType) {
            case "Savings" -> new SavingsAccount(balance, customer);
            case "Current" -> new CurrentAccount(balance, customer);
            case "ZeroBalance" -> new ZeroBalanceAccount(customer);
            default -> throw new IllegalArgumentException("Invalid account type.");
        };
        accounts.put(acc.getAccountNumber(), acc);
        return acc;
    }

    public Account[] listAccounts() {
        return accounts.values().toArray(new Account[0]);
    }

    public void calculateInterest() {
        for (Account acc : accounts.values()) {
            if (acc instanceof SavingsAccount) {
                acc.deposit(acc.getBalance() * 0.035f);
            }
        }
    }
}
