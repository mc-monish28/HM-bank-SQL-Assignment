SELECT transaction_id, account_id, amount, transaction_date, COUNT(*) AS duplicate_count 
FROM Transactions 
GROUP BY account_id, amount, transaction_date 
HAVING COUNT(*) > 1;
