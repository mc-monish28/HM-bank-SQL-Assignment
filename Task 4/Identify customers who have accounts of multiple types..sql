SELECT customer_id, COUNT(DISTINCT account_type) AS account_types
FROM Accounts
GROUP BY customer_id
HAVING COUNT(DISTINCT account_type) > 1;
