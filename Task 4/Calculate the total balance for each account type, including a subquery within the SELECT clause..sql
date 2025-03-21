SELECT account_type, 
       (SELECT SUM(balance) FROM Accounts a2 WHERE a2.account_type = a1.account_type) AS total_balance
FROM Accounts a1
GROUP BY account_type;
