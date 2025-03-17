SELECT c.first_name, c.last_name, c.email, a.account_type 
FROM Customers c
JOIN Accounts a ON c.customer_id = a.customer_id;
