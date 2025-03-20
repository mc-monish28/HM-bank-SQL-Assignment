SELECT account_type, SUM(balance) AS total_balance 
FROM Accounts 
GROUP BY account_type;
