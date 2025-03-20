SELECT t.account_id, 
       AVG(a.balance) AS avg_daily_balance
FROM Transactions t
JOIN Accounts a ON t.account_id = a.account_id
WHERE t.transaction_date BETWEEN '2024-03-01' AND '2024-03-17'
GROUP BY t.account_id;
