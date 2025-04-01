package com.bank.bean;

import java.util.Date;

public class Transaction {
    private Account account;
    private String description;
    private Date dateTime;
    private String transactionType;
    private float transactionAmount;

    public Transaction(Account account, String description, String transactionType, float transactionAmount) {
        this.account = account;
        this.description = description;
        this.dateTime = new Date();
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
    }
}
