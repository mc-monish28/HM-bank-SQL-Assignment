SELECT c.customer_id, c.first_name, c.last_name, a.account_type, SUM(a.balance) AS total_balance 
FROM Customers c 
JOIN Accounts a ON c.customer_id = a.customer_id 
GROUP BY c.customer_id, a.account_type 
HAVING total_balance > 5000;
