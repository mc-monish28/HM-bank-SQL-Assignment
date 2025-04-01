CREATE DATABASE bank_db;
USE bank_db;

CREATE TABLE customers (
    customer_id BIGINT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20)
);

CREATE TABLE accounts (
    acc_no BIGINT PRIMARY KEY,
    acc_type VARCHAR(20),
    balance FLOAT,
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE transactions (
    txn_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    acc_no BIGINT,
    txn_type VARCHAR(20),
    amount FLOAT,
    txn_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (acc_no) REFERENCES accounts(acc_no)
);
