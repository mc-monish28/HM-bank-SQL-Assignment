SELECT account_id, balance, 
       (balance * 0.05) AS interest_accrued 
FROM Accounts 
WHERE account_type = 'savings';
