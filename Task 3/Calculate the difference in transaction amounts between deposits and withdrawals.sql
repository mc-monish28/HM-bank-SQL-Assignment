SELECT ( 
    SUM(CASE WHEN transaction_type = 'deposit' THEN amount ELSE 0 END) - 
    SUM(CASE WHEN transaction_type = 'withdrawal' THEN amount ELSE 0 END) 
) AS transaction_difference 
FROM Transactions;
