SELECT SUM(amount) AS total_deposits 
FROM Transactions 
WHERE transaction_type = 'deposit' 
AND transaction_date = '2024-03-17';
