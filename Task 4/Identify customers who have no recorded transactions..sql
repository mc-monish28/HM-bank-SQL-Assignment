SELECT c.customer_id, c.first_name, c.last_name
FROM Customers c
WHERE c.customer_id NOT IN (
    SELECT DISTINCT a.customer_id
    FROM Transactions t
    JOIN Accounts a ON t.account_id = a.account_id
);
