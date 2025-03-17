SELECT c.first_name, c.last_name, c.email, t.transaction_id, t.transaction_type, t.amount, t.transaction_date 
FROM Customers c
JOIN Accounts a ON c.customer_id = a.customer_id
JOIN Transactions t ON a.account_id = t.account_id;
