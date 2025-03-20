SELECT customer_id, COUNT(account_id) AS account_count 
FROM Accounts 
GROUP BY customer_id 
HAVING COUNT(account_id) > 1;
