SELECT c.customer_id, c.first_name, c.last_name, c.email, a.account_id, a.account_type, a.balance 
FROM Customers c 
JOIN Accounts a ON c.customer_id = a.customer_id;
