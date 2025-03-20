SELECT t.transaction_id, t.account_id, t.transaction_type, t.amount, a.account_type 
FROM Transactions t 
JOIN Accounts a ON t.account_id = a.account_id;
