SELECT t.transaction_id, t.account_id, t.transaction_type, t.amount, c.customer_id, c.first_name, c.last_name, c.email 
FROM Transactions t
JOIN Accounts a ON t.account_id = a.account_id
JOIN Customers c ON a.customer_id = c.customer_id
WHERE t.account_id = 3;
